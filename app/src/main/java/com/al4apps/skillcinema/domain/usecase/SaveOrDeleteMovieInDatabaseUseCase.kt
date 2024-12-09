package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.dbmodels.GenreDb
import com.al4apps.skillcinema.data.repository.MoviesRepositoryDbImpl
import com.al4apps.skillcinema.domain.model.MovieModel
import javax.inject.Inject

class SaveOrDeleteMovieInDatabaseUseCase @Inject constructor(
    private val moviesRepositoryDb: MoviesRepositoryDbImpl,
) {
    suspend fun save(movie: MovieModel) {
        moviesRepositoryDb.saveMovieInDb(movie)
        moviesRepositoryDb.saveGenresInDb(movie.genres.map { GenreDb(genre = it.genre) })
    }

    suspend fun delete(movie: MovieModel) {
        moviesRepositoryDb.deleteMovieInDb(movie)
    }
}