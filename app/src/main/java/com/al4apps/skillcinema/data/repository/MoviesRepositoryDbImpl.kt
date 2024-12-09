package com.al4apps.skillcinema.data.repository

import com.al4apps.skillcinema.data.db.GenresDao
import com.al4apps.skillcinema.data.db.MovieToCollectionDao
import com.al4apps.skillcinema.data.db.MoviesDao
import com.al4apps.skillcinema.data.dbmodels.GenreDb
import com.al4apps.skillcinema.data.dbmodels.GenreToMovieDb
import com.al4apps.skillcinema.data.dbmodels.MovieDb
import com.al4apps.skillcinema.data.dbmodels.MovieDbCache
import com.al4apps.skillcinema.domain.model.GenreModel
import com.al4apps.skillcinema.domain.model.MovieDbModel
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.repository.MoviesRepositoryDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepositoryDbImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val genresDao: GenresDao,
    private val movieToCollectionDao: MovieToCollectionDao
) : MoviesRepositoryDb {
    override suspend fun saveMovieInDb(movie: MovieModel) {
        moviesDao.saveMovie(movie.toMovieDb())
        val genreToMovies = movie.genres.map { genreModel ->
            GenreToMovieDb(genre = genreModel.genre, movieId = movie.kinopoiskId)
        }
        saveGenreToMovies(genreToMovies)
    }

    override suspend fun cacheMovieInDb(movie: MovieModel) {
        moviesDao.cacheMovie(movie.toMovieDbCache())
    }

    override suspend fun deleteMovieInDb(movie: MovieModel) {
        moviesDao.deleteMovie(movie.toMovieDb())
    }

    override suspend fun deleteCachedMovie(movie: MovieDbModel) {
        moviesDao.deleteMovie(movie.toMovieDbCache())
    }

    override suspend fun deleteAllCachedMovies() {
        moviesDao.deleteCacheTable()
    }

    private suspend fun saveGenreToMovies(list: List<GenreToMovieDb>) {
        genresDao.saveGenreToMovies(list)
    }

    override suspend fun saveGenresInDb(genres: List<GenreDb>) {
        genresDao.saveGenres(genres)
    }

    override suspend fun saveOrDeleteMovieInCollectionDb(movieId: Int, collectionId: Int): Boolean {
        return movieToCollectionDao.updateMovieToCollection(collectionId, movieId)
    }

    override suspend fun getMovieIdsInCollectionDb(collectionId: Int): List<Int> {
        return movieToCollectionDao.getMovieIdsByCollectionId(collectionId)
    }

    override fun getMoviesInCollectionDbFlow(collectionId: Int): Flow<List<MovieDbModel>> {
        return movieToCollectionDao.getMovieIdsByCollectionIdFlow(collectionId)
            .map { list ->
                list.mapNotNull {
                    val genres = genresDao.getGenresByMovieId(it)
                    moviesDao.getMovieById(it)?.toMovieDbModel(genres)
                }
            }
    }

    override fun getCachedMoviesFlow(): Flow<List<MovieDbModel>> {
        return moviesDao.cachedMoviesFlow().map { list ->
            list.map { movieDb ->
                val genres = genresDao.getGenresByMovieId(movieDb.kinopoiskId)
                movieDb.toMovieDbModel(genres)
            }
        }
    }

    override suspend fun getCachedMovies(): List<MovieDbModel> {
        return moviesDao.getCachedMovies().map {
            val genres = genresDao.getGenresByMovieId(it.kinopoiskId)
            it.toMovieDbModel(genres)
        }
    }

    private fun MovieDb.toMovieDbModel(genres: List<GenreDb>): MovieDbModel {
        return MovieDbModel(
            kinopoiskId,
            nameRu,
            nameEn,
            posterUrl,
            ratingKinopoisk,
            genres.map { GenreModel(it.genre) },
            timestamp,
            isWatched
        )
    }

    private fun MovieDbCache.toMovieDbModel(genres: List<GenreDb>): MovieDbModel {
        return MovieDbModel(
            kinopoiskId,
            nameRu,
            nameEn,
            posterUrl,
            ratingKinopoisk,
            genres.map { GenreModel(it.genre) },
            timestamp,
            isWatched
        )
    }

    private fun MovieModel.toMovieDb(): MovieDb {
        return MovieDb(
            kinopoiskId, nameRu, nameEn, posterUrl, ratingKinopoisk, System.currentTimeMillis()
        )
    }

    private fun MovieModel.toMovieDbCache(): MovieDbCache {
        return MovieDbCache(
            kinopoiskId, nameRu, nameEn, posterUrl, ratingKinopoisk, System.currentTimeMillis()
        )
    }

    private fun MovieDbModel.toMovieDbCache(): MovieDbCache {
        return MovieDbCache(
            kinopoiskId, nameRu, nameEn, posterUrl, ratingKinopoisk, timestamp, isWatched
        )
    }
}