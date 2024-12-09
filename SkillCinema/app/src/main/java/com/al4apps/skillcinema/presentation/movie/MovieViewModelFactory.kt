package com.al4apps.skillcinema.presentation.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.AddCollectionToDbUseCase
import com.al4apps.skillcinema.domain.usecase.AddMovieToCollectionDbUseCase
import com.al4apps.skillcinema.domain.usecase.CacheMovieInDbUseCase
import com.al4apps.skillcinema.domain.usecase.GenerateNextCollectionIdUseCase
import com.al4apps.skillcinema.domain.usecase.GetCollectionsFromDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetCollectionsWithMovieUseCase
import com.al4apps.skillcinema.domain.usecase.GetMovieInfoUseCase
import com.al4apps.skillcinema.domain.usecase.GetSeriesInfoUseCase
import javax.inject.Inject

class MovieViewModelFactory @Inject constructor(
    private val getMovieInfoUseCase: GetMovieInfoUseCase,
    private val getSeriesInfoUseCase: GetSeriesInfoUseCase,
    private val cacheMovieInDbUseCase: CacheMovieInDbUseCase,
    private val getCollectionsWithMovieUseCase: GetCollectionsWithMovieUseCase,
    private val addMovieToCollectionDbUseCase: AddMovieToCollectionDbUseCase,
    private val generateNextCollectionIdUseCase: GenerateNextCollectionIdUseCase,
    private val addCollectionToDbUseCase: AddCollectionToDbUseCase,
    private val getCollectionsFromDbUseCase: GetCollectionsFromDbUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(
                getMovieInfoUseCase,
                getSeriesInfoUseCase,
                cacheMovieInDbUseCase,
                getCollectionsWithMovieUseCase,
                addMovieToCollectionDbUseCase,
                generateNextCollectionIdUseCase,
                addCollectionToDbUseCase,
                getCollectionsFromDbUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}