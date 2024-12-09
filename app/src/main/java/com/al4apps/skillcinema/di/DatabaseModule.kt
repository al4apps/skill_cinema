package com.al4apps.skillcinema.di

import android.app.Application
import androidx.room.Room
import com.al4apps.skillcinema.data.db.AppDatabase
import com.al4apps.skillcinema.data.db.CollectionsDao
import com.al4apps.skillcinema.data.db.GenresDao
import com.al4apps.skillcinema.data.db.MovieToCollectionDao
import com.al4apps.skillcinema.data.db.MoviesDao
import com.al4apps.skillcinema.data.db.PersonsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application) : AppDatabase {
        return Room.databaseBuilder(application.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DB_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesDao(db: AppDatabase): MoviesDao = db.moviesDao()

    @Provides
    @Singleton
    fun provideCollectionsDao(db: AppDatabase): CollectionsDao = db.collectionsDao()

    @Provides
    @Singleton
    fun provideMovieToCollectionsDao(db: AppDatabase): MovieToCollectionDao = db.movieToCollectionDao()

    @Provides
    @Singleton
    fun providePersonsDao(db: AppDatabase): PersonsDao = db.personsDao()

    @Provides
    @Singleton
    fun provideGenresDao(db: AppDatabase): GenresDao = db.genresDao()
}