package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MoviesRepositoryDbImpl
import com.al4apps.skillcinema.domain.model.MovieDbModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesFromDbUseCase @Inject constructor(
    private val moviesRepository: MoviesRepositoryDbImpl
) {
    fun execute(): Flow<List<MovieDbModel>> {
        return moviesRepository.getCachedMoviesFlow()
    }
}