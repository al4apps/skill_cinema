package com.al4apps.skillcinema.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.al4apps.skillcinema.data.dbmodels.CollectionDb
import com.al4apps.skillcinema.data.dbmodels.GenreDb
import com.al4apps.skillcinema.data.dbmodels.GenreToMovieDb
import com.al4apps.skillcinema.data.dbmodels.MovieDb
import com.al4apps.skillcinema.data.dbmodels.MovieDbCache
import com.al4apps.skillcinema.data.dbmodels.MovieToCollection
import com.al4apps.skillcinema.data.dbmodels.PersonDb

@Database(
    entities = [
        GenreDb::class,
        MovieDb::class,
        MovieDbCache::class,
        PersonDb::class,
        CollectionDb::class,
        GenreToMovieDb::class,
        MovieToCollection::class,
    ],
    version = AppDatabase.DB_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun collectionsDao(): CollectionsDao
    abstract fun movieToCollectionDao(): MovieToCollectionDao
    abstract fun personsDao(): PersonsDao
    abstract fun genresDao(): GenresDao

    companion object {
        const val DB_NAME = "app_database"
        const val DB_VERSION = 1
    }
}