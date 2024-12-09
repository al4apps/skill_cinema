package com.al4apps.skillcinema.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.SearchFilmsUseCase
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
private val searchFilmsUseCase: SearchFilmsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(searchFilmsUseCase) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}