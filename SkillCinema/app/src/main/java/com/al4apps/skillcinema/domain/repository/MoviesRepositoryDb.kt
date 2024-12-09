package com.al4apps.skillcinema.domain.repository

import com.al4apps.skillcinema.data.dbmodels.GenreDb
import com.al4apps.skillcinema.domain.model.MovieDbModel
import com.al4apps.skillcinema.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MoviesRepositoryDb {


    suspend fun saveMovieInDb(movie: MovieModel)
    suspend fun cacheMovieInDb(movie: MovieModel)
    suspend fun deleteMovieInDb(movie: MovieModel)

    suspend fun deleteCachedMovie(movie: MovieDbModel)
    suspend fun deleteAllCachedMovies()
    suspend fun saveGenresInDb(genres: List<GenreDb>)
    suspend fun saveOrDeleteMovieInCollectionDb(movieId: Int, collectionId: Int): Boolean
    suspend fun getMovieIdsInCollectionDb(collectionId: Int): List<Int>
    fun getMoviesInCollectionDbFlow(collectionId: Int): Flow<List<MovieDbModel>>
    fun getCachedMoviesFlow(): Flow<List<MovieDbModel>>
    suspend fun getCachedMovies(): List<MovieDbModel>
}