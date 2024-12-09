package com.al4apps.skillcinema.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.SetFirstStartFlagUseCase
import javax.inject.Inject

class OnboardingViewModelFactory @Inject constructor(
    private val setFirstStartFlagUseCase: SetFirstStartFlagUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            return OnboardingViewModel(setFirstStartFlagUseCase) as T
        }
        throw IllegalArgumentException("ViewModelNotFound")
    }
}