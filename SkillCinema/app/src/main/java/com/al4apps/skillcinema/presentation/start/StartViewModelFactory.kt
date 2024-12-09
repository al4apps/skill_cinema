package com.al4apps.skillcinema.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.CheckFirstStartUseCase
import javax.inject.Inject

class StartViewModelFactory @Inject constructor(
    private val checkFirstStartUseCase: CheckFirstStartUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartViewModel::class.java)) {
            return StartViewModel(checkFirstStartUseCase) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}