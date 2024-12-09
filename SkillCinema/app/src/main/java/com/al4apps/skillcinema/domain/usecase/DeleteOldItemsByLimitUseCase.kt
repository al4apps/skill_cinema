package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MoviesRepositoryDbImpl
import com.al4apps.skillcinema.data.repository.PersonsRepositoryDbImpl
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.model.MovieDbModel
import com.al4apps.skillcinema.domain.model.PersonDbModel
import javax.inject.Inject

class DeleteOldItemsByLimitUseCase @Inject constructor(
    private val moviesRepositoryDb: MoviesRepositoryDbImpl,
    private val personsRepositoryDb: PersonsRepositoryDbImpl
) {

    suspend fun execute() {
        val items = moviesRepositoryDb.getCachedMovies() + personsRepositoryDb.getPersons()
        if (items.size > Constants.CACHED_ITEMS_LIMIT) {
            val itemsToDelete = items.sortedByDescending { it.time }
                .takeLast(items.size - Constants.CACHED_ITEMS_LIMIT)
            itemsToDelete.forEach { item ->
                if (item is PersonDbModel) personsRepositoryDb.deleteCachedPerson(item)
                if (item is MovieDbModel) moviesRepositoryDb.deleteCachedMovie(item)
            }
        }
    }
}