package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.StartRepositoryImpl
import javax.inject.Inject

class SetFirstStartFlagUseCase @Inject constructor(
    private val startRepository: StartRepositoryImpl
) {
    suspend fun setFlag() {
        startRepository.setFirstStartFlag()
    }
}