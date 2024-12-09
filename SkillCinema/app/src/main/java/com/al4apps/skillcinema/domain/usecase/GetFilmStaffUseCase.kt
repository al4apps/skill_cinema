package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.StaffRepositoryImpl
import com.al4apps.skillcinema.domain.model.StaffModel
import javax.inject.Inject

class GetFilmStaffUseCase @Inject constructor(
    private val staffRepository: StaffRepositoryImpl
) {
    suspend fun execute(kinopoiskId: Int): List<StaffModel> {
        return staffRepository.getStaffByKinopoiskId(kinopoiskId)
    }
}