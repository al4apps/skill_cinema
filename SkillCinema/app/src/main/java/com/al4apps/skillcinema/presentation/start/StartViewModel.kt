package com.al4apps.skillcinema.presentation.start

import androidx.lifecycle.ViewModel
import com.al4apps.skillcinema.domain.usecase.CheckFirstStartUseCase
import javax.inject.Inject

class StartViewModel @Inject constructor(
    checkFirstStartUseCase: CheckFirstStartUseCase,
) : ViewModel() {
    val firstStartFlag = checkFirstStartUseCase.execute()
}