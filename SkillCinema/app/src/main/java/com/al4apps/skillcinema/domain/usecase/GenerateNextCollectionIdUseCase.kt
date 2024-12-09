package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.CollectionsRepositoryImpl
import javax.inject.Inject

class GenerateNextCollectionIdUseCase @Inject constructor(
    private val collectionsRepository: CollectionsRepositoryImpl
) {
    suspend fun execute(): Int {
        return collectionsRepository.getAllCollectionIds().maxOf { it } + 1
    }
}