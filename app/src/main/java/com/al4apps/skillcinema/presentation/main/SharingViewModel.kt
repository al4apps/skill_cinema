package com.al4apps.skillcinema.presentation.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharingViewModel: ViewModel() {

    private var isStart: Boolean = true

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    fun updateLoadingState(isLoading: Boolean) {
        _loadingState.value = isLoading
    }
}