package com.al4apps.skillcinema.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.al4apps.skillcinema.data.dbmodels.PersonDb
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePerson(persons: PersonDb)

    @Query("SELECT * FROM ${PersonDb.TABLE_NAME}")
    fun personsFlow(): Flow<List<PersonDb>>

    @Query("SELECT * FROM ${PersonDb.TABLE_NAME}")
    fun getPersons(): List<PersonDb>

    @Query("DELETE FROM ${PersonDb.TABLE_NAME}")
    suspend fun deleteAllPersons()

    @Delete
    suspend fun deletePerson(person: PersonDb)
}