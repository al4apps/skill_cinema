package com.al4apps.skillcinema.presentation.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.GetSeriesInfoUseCase
import javax.inject.Inject

class SeriesViewModelFactory @Inject constructor(
    private val getSeriesInfoUseCase: GetSeriesInfoUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SeriesViewModel::class.java)) {
            return SeriesViewModel(getSeriesInfoUseCase) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}