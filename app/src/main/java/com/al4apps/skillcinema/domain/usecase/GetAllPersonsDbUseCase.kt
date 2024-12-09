package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.PersonsRepositoryDbImpl
import com.al4apps.skillcinema.domain.model.PersonDbModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPersonsDbUseCase @Inject constructor(
    private val personsRepositoryDb: PersonsRepositoryDbImpl
) {
    fun execute(): Flow<List<PersonDbModel>> {
        return personsRepositoryDb.getPersonsDbFlow()
    }
}