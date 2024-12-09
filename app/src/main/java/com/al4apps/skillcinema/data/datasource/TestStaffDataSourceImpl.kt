package com.al4apps.skillcinema.data.datasource

import com.al4apps.skillcinema.data.models.staff.PersonDto
import com.al4apps.skillcinema.data.models.staff.StaffDto
import com.al4apps.skillcinema.domain.model.Sex
import javax.inject.Inject

class TestStaffDataSourceImpl @Inject constructor() : StaffDataSource {
    override suspend fun getFilmStaffById(kinopoiskId: Int): List<StaffDto> {
        return emptyList()
    }

    override suspend fun getPersonInfoById(id: Int): PersonDto {
        return PersonDto(111, "", "", Sex.MALE, "", "", emptyList())
    }
}