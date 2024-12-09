package com.al4apps.skillcinema.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.GetPopularMoviesUseCase
import com.al4apps.skillcinema.domain.usecase.GetPremieresUseCase
import com.al4apps.skillcinema.domain.usecase.GetMoviesCollectionByGenreAndCountryIdUseCase
import com.al4apps.skillcinema.domain.usecase.GetSeriesUseCase
import com.al4apps.skillcinema.domain.usecase.GetSimilarFilmsById
import com.al4apps.skillcinema.domain.usecase.GetTop250UseCase
import javax.inject.Inject

class MoviesViewModelFactory @Inject constructor(
    private val getPremieresUseCase: GetPremieresUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTop250UseCase: GetTop250UseCase,
    private val getMoviesCollectionByGenreAndCountryIdUseCase: GetMoviesCollectionByGenreAndCountryIdUseCase,
    private val getSeriesUseCase: GetSeriesUseCase,
    private val getSimilarFilmsById: GetSimilarFilmsById
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            return MoviesViewModel(
                getPremieresUseCase,
                getPopularMoviesUseCase,
                getTop250UseCase,
                getMoviesCollectionByGenreAndCountryIdUseCase,
                getSeriesUseCase,
                getSimilarFilmsById
            ) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}