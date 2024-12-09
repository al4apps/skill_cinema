package com.al4apps.skillcinema.data.dbmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = PersonDb.TABLE_NAME)
data class PersonDb(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = ID_NAME)
    val id: Int,
    @ColumnInfo(name = NAME_RU_NAME)
    val nameRu: String?,
    @ColumnInfo(name = NAME_EN_NAME)
    val nameEn: String?,
    @ColumnInfo(name = POSTER_URL_NAME)
    val posterUrl: String,
    @ColumnInfo(name = PROFESSION_NAME)
    val profession: String?,
    val timestamp: Long
) {

    companion object{
        const val TABLE_NAME = "person"
        const val NAME_RU_NAME = "name_ru"
        const val NAME_EN_NAME = "name_en"
        const val POSTER_URL_NAME = "poster_url"
        const val PROFESSION_NAME = "profession"
        const val ID_NAME = "id"
    }
}