package com.al4apps.skillcinema.data.repository

import com.al4apps.skillcinema.data.db.PersonsDao
import com.al4apps.skillcinema.data.dbmodels.PersonDb
import com.al4apps.skillcinema.domain.model.PersonDbModel
import com.al4apps.skillcinema.domain.model.PersonModel
import com.al4apps.skillcinema.domain.repository.PersonRepositoryDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonsRepositoryDbImpl @Inject constructor(
    private val personsDao: PersonsDao
) : PersonRepositoryDb {
    override suspend fun savePersonInDb(person: PersonModel) {
        personsDao.savePerson(person.toPersonDb())
    }

    override suspend fun deleteAllPersons() {
        personsDao.deleteAllPersons()
    }

    override suspend fun deleteCachedPerson(person: PersonDbModel) {
        personsDao.deletePerson(person.toPersonDb())
    }

    override fun getPersonsDbFlow(): Flow<List<PersonDbModel>> {
        return personsDao.personsFlow().map { list ->
            list.map { it.toPersonDbModel() }
        }
    }

    override fun getPersons(): List<PersonDbModel> {
        return personsDao.getPersons().map { it.toPersonDbModel() }
    }

    private fun PersonModel.toPersonDb(): PersonDb {
        return PersonDb(personId, nameRu, nameEn, posterUrl, profession, System.currentTimeMillis())
    }

    private fun PersonDbModel.toPersonDb(): PersonDb {
        return PersonDb(id, nameRu, nameEn, posterUrl, profession, timestamp)
    }

    private fun PersonDb.toPersonDbModel(): PersonDbModel {
        return PersonDbModel(id, nameRu, nameEn, posterUrl, profession, timestamp)
    }
}