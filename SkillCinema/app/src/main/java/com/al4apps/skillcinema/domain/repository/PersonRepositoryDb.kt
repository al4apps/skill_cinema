package com.al4apps.skillcinema.domain.repository

import com.al4apps.skillcinema.domain.model.PersonDbModel
import com.al4apps.skillcinema.domain.model.PersonModel
import kotlinx.coroutines.flow.Flow

interface PersonRepositoryDb {

    suspend fun savePersonInDb(person: PersonModel)

    suspend fun deleteAllPersons()

    suspend fun deleteCachedPerson(person: PersonDbModel)

    fun getPersonsDbFlow(): Flow<List<PersonDbModel>>

    fun getPersons(): List<PersonDbModel>
}