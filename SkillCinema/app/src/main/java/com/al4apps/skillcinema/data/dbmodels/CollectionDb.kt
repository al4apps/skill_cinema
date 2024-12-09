package com.al4apps.skillcinema.data.dbmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CollectionDb.TABLE_NAME)
data class CollectionDb(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_NAME)
    val id: Int,
    @ColumnInfo(name = NAME_STRING)
    val name: String,
    @ColumnInfo(name = IS_USERS_COLLECTION_NAME)
    val isUsersCollection: Boolean
) {
    companion object {
        const val TABLE_NAME = "movies_collection"
        const val ID_NAME = "id"
        const val NAME_STRING = "name"
        const val IS_USERS_COLLECTION_NAME = "is_users_collection"
    }
}
