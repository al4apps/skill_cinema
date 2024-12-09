package com.al4apps.skillcinema.data.datasource

import com.al4apps.skillcinema.data.models.staff.PersonDto
import com.al4apps.skillcinema.data.models.staff.StaffDto
import com.al4apps.skillcinema.data.network.KinopoiskApi
import javax.inject.Inject

class StaffDataSourceImpl @Inject constructor(
    private val kinopoiskApi: KinopoiskApi
) : StaffDataSource{
    override suspend fun getFilmStaffById(kinopoiskId: Int): List<StaffDto> {
        return kinopoiskApi.getMovieStaff(kinopoiskId)
    }

    override suspend fun getPersonInfoById(id: Int): PersonDto {
        return kinopoiskApi.getPersonInfo(id)
    }
}