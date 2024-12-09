package com.al4apps.skillcinema.presentation.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.al4apps.skillcinema.domain.model.DynamicCollectionInfo
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.model.MovieCollectionType
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.model.MoviesArgsModel
import com.al4apps.skillcinema.domain.toCollectionInfo
import com.al4apps.skillcinema.domain.usecase.GetMoviesCollectionByGenreAndCountryIdUseCase
import com.al4apps.skillcinema.domain.usecase.GetPopularMoviesUseCase
import com.al4apps.skillcinema.domain.usecase.GetPremieresUseCase
import com.al4apps.skillcinema.domain.usecase.GetSeriesUseCase
import com.al4apps.skillcinema.domain.usecase.GetSimilarFilmsById
import com.al4apps.skillcinema.domain.usecase.GetTop250UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val getPremieresUseCase: GetPremieresUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTop250UseCase: GetTop250UseCase,
    private val getMoviesCollectionByGenreAndCountryIdUseCase: GetMoviesCollectionByGenreAndCountryIdUseCase,
    private val getSeriesUseCase: GetSeriesUseCase,
    private val getSimilarFilmsById: GetSimilarFilmsById,
) : ViewModel() {

    private var isListInitialized = false
    private var argsModel: MoviesArgsModel? = null

    private val _isPagingTypeLiveData = MutableLiveData<Boolean>()
    val isPagingTypeLiveData: LiveData<Boolean>
        get() = _isPagingTypeLiveData

    private val _moviesLiveData = MutableLiveData<List<MovieModel>>()
    val moviesLveData: LiveData<List<MovieModel>>
        get() = _moviesLiveData

    private val _pagedMoviesLiveData = MutableLiveData<PagingData<MovieModel>>()
    val pagedMoviesLiveData: LiveData<PagingData<MovieModel>>
        get() = _pagedMoviesLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> get() = _isLoadingLiveData


    private val _dynamicCollectionInfo = MutableStateFlow<DynamicCollectionInfo?>(null)
    val dynamicCollectionInfo = _dynamicCollectionInfo.asStateFlow()

    fun loadMovies(argsModel: MoviesArgsModel) {
        viewModelScope.launch {
            if (!isListInitialized) {
                setMovieType(argsModel)
                if (argsModel.isPagingData()) getPagedMovies(argsModel)
                else getMovies(argsModel)

            }
        }
    }

    private fun setMovieType(argsModel: MoviesArgsModel) {
        this.argsModel = argsModel
        _isPagingTypeLiveData.value = argsModel.isPagingData()
    }

    fun refreshList() {
        argsModel?.let { getMovies(it) }
    }

    private suspend fun getPagedMovies(argsModel: MoviesArgsModel) {
        val pager = when (argsModel.type) {
            MovieCollectionType.POPULAR -> getPopularMoviesUseCase.execute()
            MovieCollectionType.TOP250 -> getTop250UseCase.execute()
            MovieCollectionType.SERIES -> getSeriesUseCase.execute()
            MovieCollectionType.DYNAMIC -> getDynamicCollection(argsModel.genreCountry)
            else -> throw IllegalArgumentException("Incorrect movie type")
        }
        pager.flow.catch { Timber.d(it) }.cachedIn(viewModelScope).collectLatest {
            _pagedMoviesLiveData.value = it
            isListInitialized = true
        }
    }

    private fun getMovies(argsModel: MoviesArgsModel) {
        viewModelScope.launch {
            try {
                _moviesLiveData.value = when (argsModel.type) {
                    MovieCollectionType.PREMIER -> getPremieresUseCase.execute()
                    MovieCollectionType.SIMILAR -> {
                        if (argsModel.kinopoiskId == MoviesFragment.NO_ID_DEFAULT) {
                            throw IllegalArgumentException("Incorrect movie ID")
                        }
                        getSimilarFilmsById.execute(argsModel.kinopoiskId)
                    }

                    else -> emptyList()
                }
                isListInitialized = true
            } catch (t: Throwable) {
                _moviesLiveData.value = emptyList()
                Timber.d(t)
            } finally {
                _isLoadingLiveData.value = false
            }
        }
    }

    private fun getDynamicCollection(
        genreCountry: GenreCountryFilter
    ): Pager<Int, MovieModel> {
        return if (genreCountry.genreId != MoviesFragment.NO_ID_DEFAULT || genreCountry.countryId != MoviesFragment.NO_ID_DEFAULT) {
            val filter = GenreCountryFilter(genreCountry.genreId, genreCountry.countryId)
            setTitle(filter)
            getMoviesCollectionByGenreAndCountryIdUseCase.execute(filter)
        } else throw IllegalArgumentException("Incorrect genre or country id")
    }

    private fun setTitle(genreCountryFilter: GenreCountryFilter) {
        _dynamicCollectionInfo.value = genreCountryFilter.toCollectionInfo().dynamicCollectionInfo
    }

    private fun MoviesArgsModel.isPagingData(): Boolean {
        return !(type == MovieCollectionType.PREMIER || type == MovieCollectionType.SIMILAR)
    }
}