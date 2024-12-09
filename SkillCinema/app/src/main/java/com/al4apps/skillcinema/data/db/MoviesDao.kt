package com.al4apps.skillcinema.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.al4apps.skillcinema.data.dbmodels.MovieDb
import com.al4apps.skillcinema.data.dbmodels.MovieDbCache
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("SELECT * FROM ${MovieDbCache.TABLE_NAME} ORDER BY ${MovieDbCache.TIMESTAMP_NAME} DESC")
    fun cachedMoviesFlow(): Flow<List<MovieDbCache>>

    @Query("SELECT * FROM ${MovieDbCache.TABLE_NAME} ORDER BY ${MovieDbCache.TIMESTAMP_NAME} DESC")
    fun getCachedMovies(): List<MovieDbCache>


    @Query("SELECT * FROM ${MovieDb.TABLE_NAME} WHERE ${MovieDb.KINOPOISK_ID_NAME} = :movieId")
    suspend fun getMovieById(movieId: Int): MovieDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie: MovieDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheMovie(movie: MovieDbCache)

    @Delete
    suspend fun deleteMovie(movieDb: MovieDb)

    @Delete
    suspend fun deleteMovie(movieDb: MovieDbCache)

    @Query("DELETE FROM ${MovieDbCache.TABLE_NAME}")
    suspend fun deleteCacheTable()
}