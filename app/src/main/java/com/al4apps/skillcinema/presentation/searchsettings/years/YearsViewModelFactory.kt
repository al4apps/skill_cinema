package com.al4apps.skillcinema.presentation.searchsettings.years

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class YearsViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(YearsViewModel::class.java)) {
            return YearsViewModel() as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}