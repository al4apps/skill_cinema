package com.al4apps.skillcinema.presentation.personfilms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.GetPersonInfoUseCase
import javax.inject.Inject

class FilmsWithPersonViewModelFactory @Inject constructor(
    private val getPersonInfoUseCase: GetPersonInfoUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmsWithPersonViewModel::class.java)) {
            return FilmsWithPersonViewModel(getPersonInfoUseCase) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}