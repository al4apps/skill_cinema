package com.al4apps.skillcinema.data.repository

import com.al4apps.skillcinema.data.db.CollectionsDao
import com.al4apps.skillcinema.data.db.MovieToCollectionDao
import com.al4apps.skillcinema.data.dbmodels.CollectionDb
import com.al4apps.skillcinema.data.dbmodels.MovieToCollection
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.model.Collection
import com.al4apps.skillcinema.domain.repository.CollectionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class CollectionsRepositoryImpl @Inject constructor(
    private val collectionsDao: CollectionsDao,
    private val movieToCollectionDao: MovieToCollectionDao,
) : CollectionsRepository {
    override suspend fun addCollection(collectionDb: CollectionDb) {
        collectionsDao.addCollection(collectionDb)
    }

    override suspend fun getAllCollections(): List<Collection> {
        return collectionsDao.getAllCollections().map { it.toCollectionModel() }
    }

    override suspend fun getCollectionById(collectionId: Int): Collection {
        return collectionsDao.getCollection(collectionId).toCollectionModel()
    }

    override suspend fun deleteCollectionById(collectionId: Int) {
        collectionsDao.deleteCollection(collectionId)
    }

    override fun getAllCollectionsFlow(): Flow<List<Collection>> {
        return collectionsDao.getAllCollectionsFlow().map { list ->
            list.map { it.toCollectionModel() }
        }
    }

    override fun getAllMovieToCollectionsFlow(): Flow<List<MovieToCollection>> {
        return movieToCollectionDao.allMovieToCollections()
    }

    override suspend fun getAllBasicCollections(): List<Collection> {
        return collectionsDao.getAllBasicCollections().map { it.toCollectionModel() }
    }

    override suspend fun getAllCollectionIds(): List<Int> {
        return collectionsDao.getAllCollectionIds()
    }

    override fun getCollectionsWithMovieId(movieId: Int): Flow<List<Int>> {
        return movieToCollectionDao.getCollectionsWithMovieId(movieId)
    }

    private fun CollectionDb.toCollectionModel(): Collection {
        return Collection(id, name, isUsersCollection = isUsersCollection)
    }
}