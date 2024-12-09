package com.al4apps.skillcinema.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.skillcinema.domain.usecase.SetFirstStartFlagUseCase
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class OnboardingViewModel @Inject constructor(
    private val setFirstStartFlagUseCase: SetFirstStartFlagUseCase,
) : ViewModel() {

    fun setFirstStartFlag() {
        viewModelScope.launch {
            try {
                setFirstStartFlagUseCase.setFlag()
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }
}