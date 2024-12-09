package com.al4apps.skillcinema.data.datasource

import com.al4apps.skillcinema.data.models.staff.PersonDto
import com.al4apps.skillcinema.data.models.staff.StaffDto

interface StaffDataSource {
    suspend fun getFilmStaffById(kinopoiskId: Int): List<StaffDto>

    suspend fun getPersonInfoById(id: Int): PersonDto
}