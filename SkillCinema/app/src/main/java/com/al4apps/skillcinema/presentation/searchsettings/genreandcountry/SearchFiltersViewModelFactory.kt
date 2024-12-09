package com.al4apps.skillcinema.presentation.searchsettings.genreandcountry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.GetGenresAndCountriesUseCase
import javax.inject.Inject

class SearchFiltersViewModelFactory @Inject constructor(
    private val getGenresAndCountriesUseCase: GetGenresAndCountriesUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchFiltersViewModel::class.java)) {
            return SearchFiltersViewModel(getGenresAndCountriesUseCase) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}