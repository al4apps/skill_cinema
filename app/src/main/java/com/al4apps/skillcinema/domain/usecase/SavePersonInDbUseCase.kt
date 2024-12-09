package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.PersonsRepositoryDbImpl
import com.al4apps.skillcinema.domain.model.PersonModel
import javax.inject.Inject

class SavePersonInDbUseCase @Inject constructor(
    private val personsRepositoryDb: PersonsRepositoryDbImpl
) {

    suspend fun save(person: PersonModel) {
        personsRepositoryDb.savePersonInDb(person)
    }
}