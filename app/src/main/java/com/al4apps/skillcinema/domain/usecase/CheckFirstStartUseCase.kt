package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.StartRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckFirstStartUseCase @Inject constructor(
    private val startRepository: StartRepositoryImpl
) {
    fun execute(): Flow<Boolean> = startRepository.getFirstStartFlag()
}