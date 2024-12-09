package com.al4apps.skillcinema.data.dbmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = MovieToCollection.TABLE_NAME)
data class MovieToCollection(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_NAME)
    val id: Int? = null,
    @ColumnInfo(name = MOVIE_ID_NAME)
    val movieId: Int,
    @ColumnInfo(name = COLLECTION_ID_NAME)
    val collectionId: Int
) {
    companion object {
        const val TABLE_NAME = "movie_to_collection"
        const val ID_NAME = "id"
        const val MOVIE_ID_NAME = "movie_id"
        const val COLLECTION_ID_NAME = "collection_id"
    }
}
