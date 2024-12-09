package com.al4apps.skillcinema.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.al4apps.skillcinema.data.dbmodels.CollectionDb
import com.al4apps.skillcinema.data.dbmodels.MovieToCollection
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieToCollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToCollection(movieToCollection: MovieToCollection)

    @Query(
        "SELECT * FROM ${MovieToCollection.TABLE_NAME} WHERE " +
                "(${MovieToCollection.COLLECTION_ID_NAME} = :collectionId) AND " +
                "(${MovieToCollection.MOVIE_ID_NAME} = :movieId)"
    )
    suspend fun getMovieToCollection(collectionId: Int, movieId: Int): MovieToCollection?

    @Query(
        "SELECT ${MovieToCollection.MOVIE_ID_NAME} FROM ${MovieToCollection.TABLE_NAME} WHERE " +
                "(${MovieToCollection.COLLECTION_ID_NAME} = :collectionId)"
    )
    suspend fun getMovieIdsByCollectionId(collectionId: Int): List<Int>

    @Query(
        "SELECT ${MovieToCollection.MOVIE_ID_NAME} FROM ${MovieToCollection.TABLE_NAME} WHERE " +
                "(${MovieToCollection.COLLECTION_ID_NAME} = :collectionId)"
    )
    fun getMovieIdsByCollectionIdFlow(collectionId: Int): Flow<List<Int>>

    @Query("SELECT * FROM ${MovieToCollection.TABLE_NAME}")
    fun allMovieToCollections(): Flow<List<MovieToCollection>>

    @Transaction
    suspend fun updateMovieToCollection(collectionId: Int, movieId: Int): Boolean {
        val isInCollection = getMovieToCollection(collectionId, movieId) != null
        return if (isInCollection) {
            deleteMovieToCollection(collectionId, movieId)
            false
        }
        else {
            addMovieToCollection(MovieToCollection(movieId = movieId, collectionId = collectionId))
            true
        }
    }

    @Query("DELETE FROM ${MovieToCollection.TABLE_NAME} WHERE" +
            "(${MovieToCollection.COLLECTION_ID_NAME} = :collectionId) AND " +
            "(${MovieToCollection.MOVIE_ID_NAME} = :movieId)")
    suspend fun deleteMovieToCollection(collectionId: Int, movieId: Int)

    @Query("SELECT ${MovieToCollection.COLLECTION_ID_NAME} FROM ${MovieToCollection.TABLE_NAME} " +
            "WHERE ${MovieToCollection.MOVIE_ID_NAME} = :movieId")
    fun getCollectionsWithMovieId(movieId: Int): Flow<List<Int>>
}