package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MoviesRepositoryDbImpl
import com.al4apps.skillcinema.data.repository.PersonsRepositoryDbImpl
import javax.inject.Inject

class CleanCacheUseCase @Inject constructor(
    private val moviesRepositoryDb: MoviesRepositoryDbImpl,
    private val personsRepositoryDb: PersonsRepositoryDbImpl
) {

    suspend fun clean() {
        moviesRepositoryDb.deleteAllCachedMovies()
        personsRepositoryDb.deleteAllPersons()
    }
}