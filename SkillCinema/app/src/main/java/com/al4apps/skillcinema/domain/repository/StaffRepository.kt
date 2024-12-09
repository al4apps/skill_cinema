package com.al4apps.skillcinema.domain.repository

import com.al4apps.skillcinema.domain.model.PersonModel
import com.al4apps.skillcinema.domain.model.StaffModel

interface StaffRepository {
    suspend fun getStaffByKinopoiskId(id: Int): List<StaffModel>

    suspend fun getStaffInfoById(staffId: Int): PersonModel
}