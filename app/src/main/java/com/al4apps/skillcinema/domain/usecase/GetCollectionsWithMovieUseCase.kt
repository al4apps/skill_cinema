package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.CollectionsRepositoryImpl
import com.al4apps.skillcinema.domain.model.Collection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionsWithMovieUseCase @Inject constructor(
    private val collectionsRepository: CollectionsRepositoryImpl
) {

    fun execute(movieId: Int): Flow<List<Int>> {
        return collectionsRepository.getCollectionsWithMovieId(movieId)
    }
}