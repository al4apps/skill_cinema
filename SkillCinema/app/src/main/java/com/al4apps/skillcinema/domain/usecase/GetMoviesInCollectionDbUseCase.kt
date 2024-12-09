package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MoviesRepositoryDbImpl
import com.al4apps.skillcinema.data.repository.MoviesRepositoryImpl
import com.al4apps.skillcinema.domain.model.MovieDbModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesInCollectionDbUseCase @Inject constructor(
    private val moviesRepository: MoviesRepositoryDbImpl
) {

    suspend fun execute(collectionId: Int): List<MovieDbModel> {
        return emptyList()
    }

    suspend fun executeOnlyIds(collectionId: Int): List<Int> {
        return moviesRepository.getMovieIdsInCollectionDb(collectionId)
    }

    fun executeFlow(collectionId: Int): Flow<List<MovieDbModel>> {
        return moviesRepository.getMoviesInCollectionDbFlow(collectionId)
    }
}