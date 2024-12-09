package com.al4apps.skillcinema.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.al4apps.skillcinema.data.dbmodels.CollectionDb
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionsDao {

    @Query("SELECT * FROM ${CollectionDb.TABLE_NAME}")
    suspend fun getAllCollections(): List<CollectionDb>

    @Query("SELECT * FROM ${CollectionDb.TABLE_NAME}")
    fun getAllCollectionsFlow(): Flow<List<CollectionDb>>

    @Query("SELECT * FROM ${CollectionDb.TABLE_NAME} WHERE ${CollectionDb.ID_NAME} = :id")
    suspend fun getCollection(id: Int): CollectionDb

    @Query("SELECT * FROM ${CollectionDb.TABLE_NAME} WHERE ${CollectionDb.IS_USERS_COLLECTION_NAME} = 0")
    suspend fun getAllBasicCollections(): List<CollectionDb>

    @Query("SELECT ${CollectionDb.ID_NAME} FROM ${CollectionDb.TABLE_NAME}")
    suspend fun getAllCollectionIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCollection(collectionDb: CollectionDb)

    @Query("DELETE FROM ${CollectionDb.TABLE_NAME} WHERE ${CollectionDb.ID_NAME} = :collectionId")
    suspend fun deleteCollection(collectionId: Int)
}