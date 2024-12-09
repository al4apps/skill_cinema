package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.CollectionsRepositoryImpl
import javax.inject.Inject

class DeleteCollectionsUseCase @Inject constructor(
    private val collectionsRepository: CollectionsRepositoryImpl
) {
    suspend fun execute(collectionId: Int) {
        collectionsRepository.deleteCollectionById(collectionId)
    }
}