package com.al4apps.skillcinema.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.al4apps.skillcinema.domain.model.FilmType
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.model.Order
import com.al4apps.skillcinema.domain.model.QuerySearch
import com.al4apps.skillcinema.domain.usecase.SearchFilmsUseCase
import com.al4apps.skillcinema.presentation.searchsettings.MovieType
import com.al4apps.skillcinema.presentation.searchsettings.SearchParams
import com.al4apps.skillcinema.presentation.searchsettings.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchFilmsUseCase: SearchFilmsUseCase
) : ViewModel() {

    private var searchParams: SearchParams = BasicSearchParams.basicSearchParams
    private var isListInitialized = false

    private val _resultsFlow = MutableStateFlow<PagingData<MovieModel>>(PagingData.empty())
    val resultsFlow = _resultsFlow.asStateFlow()

    fun saveSearchParams(searchParams: SearchParams) {
        this.searchParams = searchParams
    }

    fun search(textFlow: Flow<String>) {
        if (isListInitialized) return
        textFlow
            .debounce(700)
            .distinctUntilChanged()
            .filter { it.isNotBlank() }
            .mapLatest { searchFilms(it) }
            .launchIn(viewModelScope)
    }

    private suspend fun searchFilms(text: String) {
        try {
            val querySearch = searchParams.toQuerySearch(text)
            searchFilmsUseCase.execute(querySearch)
                .flow
                .cachedIn(viewModelScope)
                .collectLatest {
                    _resultsFlow.value = it
                    isListInitialized = true
                }
        } catch (t: Throwable) {
            Timber.d(t)
        }
    }

    private fun SearchParams.toQuerySearch(text: String): QuerySearch {
        return QuerySearch(
            genreId = genre?.genreNumber,
            countryId = country?.countryNumber,
            order = sortType.toOrder(),
            type = movieType.toFilmType(),
            ratingFrom = ratingRange.min.toInt(),
            ratingTo = ratingRange.max.toInt(),
            yearFrom = yearsRange.min?.year,
            yearTo = yearsRange.max?.year,
            keyword = text
        )
    }

    private fun SortType.toOrder(): Order {
        return when {
            this == SortType.DATE -> Order.YEAR
            this == SortType.POPULARITY -> Order.NUM_VOTE
            else -> Order.RATING
        }
    }

    private fun MovieType.toFilmType(): FilmType {
        return when {
            this == MovieType.FILM -> FilmType.FILM
            this == MovieType.SERIES -> FilmType.TV_SERIES
            else -> FilmType.ALL
        }
    }
}