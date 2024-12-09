package com.al4apps.skillcinema.presentation.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.model.Collection
import com.al4apps.skillcinema.domain.model.CollectionFooter
import com.al4apps.skillcinema.domain.model.CollectionHeader
import com.al4apps.skillcinema.domain.model.CollectionListModel
import com.al4apps.skillcinema.domain.model.CollectionToMovie
import com.al4apps.skillcinema.domain.model.MovieDetailModel
import com.al4apps.skillcinema.domain.model.MovieDetailPageModel
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.model.SeriesInfoModel
import com.al4apps.skillcinema.domain.usecase.AddCollectionToDbUseCase
import com.al4apps.skillcinema.domain.usecase.AddMovieToCollectionDbUseCase
import com.al4apps.skillcinema.domain.usecase.CacheMovieInDbUseCase
import com.al4apps.skillcinema.domain.usecase.GenerateNextCollectionIdUseCase
import com.al4apps.skillcinema.domain.usecase.GetCollectionsFromDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetCollectionsWithMovieUseCase
import com.al4apps.skillcinema.domain.usecase.GetMovieInfoUseCase
import com.al4apps.skillcinema.domain.usecase.GetSeriesInfoUseCase
import com.al4apps.skillcinema.domain.usecase.SaveOrDeleteMovieInDatabaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MovieViewModel @Inject constructor(
    private val getMovieInfoUseCase: GetMovieInfoUseCase,
    private val getSeriesInfoUseCase: GetSeriesInfoUseCase,
    private val cacheMovieInDbUseCase: CacheMovieInDbUseCase,
    private val getCollectionsWithMovieUseCase: GetCollectionsWithMovieUseCase,
    private val addMovieToCollectionDbUseCase: AddMovieToCollectionDbUseCase,
    private val generateNextCollectionIdUseCase: GenerateNextCollectionIdUseCase,
    private val addCollectionToDbUseCase: AddCollectionToDbUseCase,
    private val getCollectionsFromDbUseCase: GetCollectionsFromDbUseCase
) : ViewModel() {

    private var isFirstInit = true
    private var movieDetailModel: MovieDetailModel? = null

    private val _movieInfo = MutableStateFlow<MovieDetailPageModel?>(null)
    val movieInfo = _movieInfo.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _seriesInfoLiveData = MutableLiveData<SeriesInfoModel>()
    val seriesInfoLiveData: LiveData<SeriesInfoModel>
        get() = _seriesInfoLiveData

    private val _collectionIdsWithMovieLiveData = MutableLiveData<MovieMutableInfo>()
    val collectionIdsWithMovieLiveData: LiveData<MovieMutableInfo>
        get() = _collectionIdsWithMovieLiveData

    private val _collectionToMoviesLiveData = MutableLiveData<List<CollectionListModel>>()
    val collectionToMoviesLiveData: LiveData<List<CollectionListModel>>
        get() = _collectionToMoviesLiveData

    fun getMovieInfo(kinopoiskId: Int) {
        viewModelScope.launch {
            try {
                if (isFirstInit) _loadingState.value = true
                val model = getMovieInfoUseCase.execute(kinopoiskId)
                movieDetailModel = model.movieDetail
                _movieInfo.value = model
                _loadingState.value = false
                if (model.isSeries) getSeriesInfo(kinopoiskId)
                getCollectionsWithMovie()
                cacheMovie(model.movieDetail)
                isFirstInit = false
            } catch (t: Throwable) {
                Timber.d(t)
            } finally {
                _loadingState.value = false
            }
        }
    }

    private fun getCollectionsWithMovie() {
        movieDetailModel?.let { model ->
            getCollectionsWithMovieUseCase.execute(model.kinopoiskId)
                .catch { Timber.d(it) }
                .mapLatest { list ->
                    val info = MovieMutableInfo(
                        isLiked = list.contains(Constants.MOVIES_COLLECTION_LIKES_ID),
                        isToWatch = list.contains(Constants.MOVIES_COLLECTION_TO_WATCH_ID),
                        isWatched = list.contains(Constants.MOVIES_COLLECTION_WATCHED_ID),
                    )
                    _collectionIdsWithMovieLiveData.value = info
                }
                .launchIn(viewModelScope)
        }
    }

    private fun getSeriesInfo(kinopoiskId: Int) {
        viewModelScope.launch {
            try {
                val seriesModel = getSeriesInfoUseCase.execute(kinopoiskId)
                val seriesInfo = SeriesInfoModel(
                    seriesModel.seasons.size,
                    seriesModel.seasons.sumOf { it.episodes.size })
                _seriesInfoLiveData.value = seriesInfo
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun getCollectionToMoviesList(id: Int) {
        combine(
            getCollectionsFromDbUseCase.executeFlow(),
            getCollectionsWithMovieUseCase.execute(id)
        ) { all: List<Collection>, idsWithMovie: List<Int> ->
            val list = all.map { collection ->
                CollectionToMovie(collection, id, idsWithMovie.contains(collection.id))
            }
            val header = CollectionHeader()
            val footer = CollectionFooter()
            _collectionToMoviesLiveData.value = listOf(header) + list + footer
        }
            .catch { Timber.d(it) }
            .launchIn(viewModelScope)
    }

    fun addMovieToCollection(collectionId: Int) {
        viewModelScope.launch {
            movieDetailModel?.let { model ->
                if (collectionId == Constants.MOVIES_COLLECTION_WATCHED_ID) {
                    model.isWatched = !model.isWatched
                }
                addMovieToCollectionDbUseCase.add(model.toMovieModel(), collectionId)
            }
        }
    }

    @Transaction
    fun addNewCollection(name: String) {
        viewModelScope.launch {
            val id = generateNextCollectionIdUseCase.execute()
            val collection = Collection(id, name, isUsersCollection = true)
            addCollectionToDbUseCase.add(collection)
        }
    }

    private suspend fun cacheMovie(movie: MovieDetailModel) {
        viewModelScope.launch(Dispatchers.IO) {
            cacheMovieInDbUseCase.toCache(movie.toMovieModel())
        }
    }

    private fun MovieDetailModel.toMovieModel(): MovieModel {
        return MovieModel(
            kinopoiskId = kinopoiskId,
            nameRu = nameRu,
            nameEn = nameEn,
            year = year,
            posterUrl = posterUrl,
            posterUrlPreview = posterUrlPreview,
            countries = countries,
            genres = genres,
            duration = duration,
            premiereRu = premiereRu,
            rating = rating,
            ratingKinopoisk = ratingKinopoisk,
            ratingImdb = ratingImdb
        )
    }
}