package com.al4apps.skillcinema.presentation.moviesdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.GetCollectionsFromDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetMoviesInCollectionDbUseCase
import javax.inject.Inject

class MoviesDbViewModelFactory @Inject constructor(
    private val getMoviesInCollectionDbUseCase: GetMoviesInCollectionDbUseCase,
    private val getCollectionsFromDbUseCase: GetCollectionsFromDbUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesDbViewModel::class.java)) {
            return MoviesDbViewModel(
                getMoviesInCollectionDbUseCase,
                getCollectionsFromDbUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}