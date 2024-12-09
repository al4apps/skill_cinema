package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MoviesRepositoryDbImpl
import com.al4apps.skillcinema.domain.model.MovieModel
import javax.inject.Inject

class CacheMovieInDbUseCase @Inject constructor(
    private val moviesRepositoryDb: MoviesRepositoryDbImpl,
    private val deleteOldItemsByLimitUseCase: DeleteOldItemsByLimitUseCase
) {
    suspend fun toCache(movieModel: MovieModel) {
        moviesRepositoryDb.cacheMovieInDb(movieModel)
        deleteOldItemsByLimitUseCase.execute()
    }
}