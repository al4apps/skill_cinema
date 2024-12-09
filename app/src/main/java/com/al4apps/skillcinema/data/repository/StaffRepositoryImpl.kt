package com.al4apps.skillcinema.data.repository

import com.al4apps.skillcinema.data.datasource.StaffDataSourceImpl
import com.al4apps.skillcinema.data.models.staff.FilmWithPersonDto
import com.al4apps.skillcinema.data.models.staff.PersonDto
import com.al4apps.skillcinema.data.models.staff.StaffDto
import com.al4apps.skillcinema.domain.model.FilmWithPersonModel
import com.al4apps.skillcinema.domain.model.PersonModel
import com.al4apps.skillcinema.domain.model.StaffModel
import com.al4apps.skillcinema.domain.repository.StaffRepository
import javax.inject.Inject

class StaffRepositoryImpl @Inject constructor(
    private val staffDataSource: StaffDataSourceImpl
) : StaffRepository {
    override suspend fun getStaffByKinopoiskId(id: Int): List<StaffModel> {
        return staffDataSource.getFilmStaffById(id).map { it.toStaffModel() }
    }

    override suspend fun getStaffInfoById(personId: Int): PersonModel {
        return staffDataSource.getPersonInfoById(personId).toPersonModel()
    }

    private fun StaffDto.toStaffModel(): StaffModel {
        return StaffModel(
            staffId, nameRu, nameEn, description, posterUrl, professionText, professionKey
        )
    }

    private fun PersonDto.toPersonModel(): PersonModel {
        return PersonModel(
            personId,
            nameRu,
            nameEn,
            sex,
            posterUrl,
            profession,
            films = films.map { it.toFilmsWithPersonModel() }
        )
    }

    private fun FilmWithPersonDto.toFilmsWithPersonModel(): FilmWithPersonModel {
        return FilmWithPersonModel(
            filmId, nameRu, nameEn, rating, general, description, professionKey
        )
    }
}