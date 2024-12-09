package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.CollectionsRepositoryImpl
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.model.Collection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GetCollectionsFromDbUseCase @Inject constructor(
    private val collectionsRepository: CollectionsRepositoryImpl,
) {

    suspend fun execute(isOnlyBasicCollections: Boolean = false): List<Collection> {
        return if (isOnlyBasicCollections) collectionsRepository.getAllBasicCollections()
        else collectionsRepository.getAllCollections()
    }

    suspend fun execute(collectionId: Int): Collection {
        return collectionsRepository.getCollectionById(collectionId)
    }

    fun executeFlow(): Flow<List<Collection>> {
        val movieToCollectionsFlow = collectionsRepository.getAllMovieToCollectionsFlow()
        val allCollectionsFlow = collectionsRepository.getAllCollectionsFlow().map { list ->
            list.filter { it.id != Constants.MOVIES_COLLECTION_WATCHED_ID }
        }
        return combine(
            movieToCollectionsFlow,
            allCollectionsFlow
        ) { movieToCollections, collections ->
            collections.map { collection ->
                Collection(collection.id,
                    collection.name,
                    collection.isUsersCollection,
                    collection.drawableRes,
                    movieToCollections.count { it.collectionId == collection.id })
            }
        }
    }
}