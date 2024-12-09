package com.al4apps.skillcinema.data.dbmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.al4apps.skillcinema.domain.model.Genre

@Entity(tableName = MovieDb.TABLE_NAME)
data class MovieDb(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = KINOPOISK_ID_NAME)
    val kinopoiskId: Int,
    @ColumnInfo(name = NAME_RU_NAME)
    val nameRu: String?,
    @ColumnInfo(name = NAME_EN_NAME)
    val nameEn: String?,
    @ColumnInfo(name = POSTER_URL_NAME)
    val posterUrl: String,
    @ColumnInfo(name = RATING_KINOPOISK_NAME)
    val ratingKinopoisk: Float?,
    @ColumnInfo(name = TIMESTAMP_NAME)
    val timestamp: Long,
    @ColumnInfo(name = IS_WATCHED_NAME)
    var isWatched: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "movie"
        const val KINOPOISK_ID_NAME = "kinopoisk_id"
        const val NAME_RU_NAME = "name_ru"
        const val NAME_EN_NAME = "name_en"
        const val IS_WATCHED_NAME = "has_watched"
        const val POSTER_URL_NAME = "poster_url"
        const val RATING_KINOPOISK_NAME = "rating_kinopoisk"
        const val TIMESTAMP_NAME = "timestamp"
    }
}

@Entity(tableName = MovieDbCache.TABLE_NAME)
data class MovieDbCache(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = KINOPOISK_ID_NAME)
    val kinopoiskId: Int,
    @ColumnInfo(name = NAME_RU_NAME)
    val nameRu: String?,
    @ColumnInfo(name = NAME_EN_NAME)
    val nameEn: String?,
    @ColumnInfo(name = POSTER_URL_NAME)
    val posterUrl: String,
    @ColumnInfo(name = RATING_KINOPOISK_NAME)
    val ratingKinopoisk: Float?,
    @ColumnInfo(name = TIMESTAMP_NAME)
    val timestamp: Long,
    @ColumnInfo(name = IS_WATCHED_NAME)
    var isWatched: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "movie_cache"
        const val KINOPOISK_ID_NAME = "kinopoisk_id"
        const val NAME_RU_NAME = "name_ru"
        const val NAME_EN_NAME = "name_en"
        const val IS_WATCHED_NAME = "has_watched"
        const val POSTER_URL_NAME = "poster_url"
        const val RATING_KINOPOISK_NAME = "rating_kinopoisk"
        const val TIMESTAMP_NAME = "timestamp"
    }
}

@Entity(tableName = GenreDb.TABLE_NAME)
data class GenreDb(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = GENRE_NAME)
    override val genre: String
) : Genre {
    companion object {
        const val TABLE_NAME = "genre_table"
        const val GENRE_NAME = "genre"
    }
}

@Entity(tableName = GenreToMovieDb.TABLE_NAME)
data class GenreToMovieDb(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_NAME)
    val id: Int? = null,
    @ColumnInfo(name = GENRE_NAME)
    val genre: String,
    @ColumnInfo(name = MOVIE_ID_NAME)
    val movieId: Int,
) {
    companion object {
        const val TABLE_NAME = "genre_to_movie"
        const val ID_NAME = "id"
        const val GENRE_NAME = "genre"
        const val MOVIE_ID_NAME = "movie_id"
    }
}