package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.dbmodels.CollectionDb
import com.al4apps.skillcinema.data.repository.CollectionsRepositoryImpl
import com.al4apps.skillcinema.domain.model.Collection
import javax.inject.Inject

class AddCollectionToDbUseCase @Inject constructor(
    private val collectionsRepository: CollectionsRepositoryImpl
) {
    suspend fun add(collection: Collection) {
        collectionsRepository.addCollection(collection.toCollectionDb())
    }

    private fun Collection.toCollectionDb(): CollectionDb {
        return CollectionDb(id, name, isUsersCollection = isUsersCollection)
    }
}