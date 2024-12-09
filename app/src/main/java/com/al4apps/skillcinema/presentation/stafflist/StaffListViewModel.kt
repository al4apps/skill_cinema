package com.al4apps.skillcinema.presentation.stafflist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.StaffModel
import com.al4apps.skillcinema.domain.model.StaffType
import com.al4apps.skillcinema.domain.usecase.GetFilmStaffUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class StaffListViewModel @Inject constructor(
    private val getFilmStaffUseCase: GetFilmStaffUseCase
) : ViewModel() {

    private val _staffList = MutableStateFlow<List<StaffModel>>(emptyList())
    val staffList = _staffList.asStateFlow()
    fun getStaff(kinopoiskId: Int, type: StaffType) {
        viewModelScope.launch {
            try {
                val staff = getFilmStaffUseCase.execute(kinopoiskId)
                _staffList.value = if (type == StaffType.ACTOR) {
                    staff.filter { it.professionKey == ApiSettings.Staff.PROFESSION_KEY_ACTOR }
                } else {
                    staff.filter { it.professionKey != ApiSettings.Staff.PROFESSION_KEY_ACTOR }
                }
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }
}