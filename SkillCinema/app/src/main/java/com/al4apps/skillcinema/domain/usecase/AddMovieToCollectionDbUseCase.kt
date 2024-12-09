package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MoviesRepositoryDbImpl
import com.al4apps.skillcinema.domain.model.MovieModel
import timber.log.Timber
import javax.inject.Inject

class AddMovieToCollectionDbUseCase @Inject constructor(
    private val moviesRepository: MoviesRepositoryDbImpl,
    private val saveOrDeleteMovieInDatabaseUseCase: SaveOrDeleteMovieInDatabaseUseCase
) {

    suspend fun add(movie: MovieModel, collectionId: Int) {
        val isAdded = moviesRepository.saveOrDeleteMovieInCollectionDb(movie.kinopoiskId, collectionId)
        if (isAdded) saveOrDeleteMovieInDatabaseUseCase.save(movie)
        else saveOrDeleteMovieInDatabaseUseCase.delete(movie)
    }
}