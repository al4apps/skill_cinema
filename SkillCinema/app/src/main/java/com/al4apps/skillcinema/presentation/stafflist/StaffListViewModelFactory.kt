package com.al4apps.skillcinema.presentation.stafflist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.GetFilmStaffUseCase
import javax.inject.Inject

class StaffListViewModelFactory @Inject constructor(
    private val getFilmStaffUseCase: GetFilmStaffUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StaffListViewModel::class.java)) {
            return StaffListViewModel(getFilmStaffUseCase) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}