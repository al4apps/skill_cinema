package com.al4apps.skillcinema.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.GetMovieCollectionsListUseCase
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val getMovieCollectionsListUseCase: GetMovieCollectionsListUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(getMovieCollectionsListUseCase) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}