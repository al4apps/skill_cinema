package com.al4apps.skillcinema.domain.repository

import com.al4apps.skillcinema.data.dbmodels.CollectionDb
import com.al4apps.skillcinema.data.dbmodels.MovieToCollection
import com.al4apps.skillcinema.domain.model.Collection
import kotlinx.coroutines.flow.Flow

interface CollectionsRepository {

    suspend fun addCollection(collectionDb: CollectionDb)

    suspend fun getAllCollections(): List<Collection>

    suspend fun getCollectionById(collectionId: Int): Collection

    suspend fun deleteCollectionById(collectionId: Int)

    fun getAllCollectionsFlow(): Flow<List<Collection>>

    fun getAllMovieToCollectionsFlow(): Flow<List<MovieToCollection>>

    suspend fun getAllBasicCollections(): List<Collection>

    suspend fun getAllCollectionIds(): List<Int>

    fun getCollectionsWithMovieId(movieId: Int): Flow<List<Int>>
}