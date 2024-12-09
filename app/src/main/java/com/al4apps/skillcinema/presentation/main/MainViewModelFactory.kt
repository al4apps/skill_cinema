package com.al4apps.skillcinema.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.AddCollectionToDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetCollectionsFromDbUseCase
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val addCollectionToDbUseCase: AddCollectionToDbUseCase,
    private val getCollectionsFromDbUseCase: GetCollectionsFromDbUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(addCollectionToDbUseCase, getCollectionsFromDbUseCase) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}