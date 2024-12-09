package com.al4apps.skillcinema.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.al4apps.skillcinema.data.dbmodels.GenreDb
import com.al4apps.skillcinema.data.dbmodels.GenreToMovieDb

@Dao
interface GenresDao {

    @Query("SELECT ${GenreToMovieDb.GENRE_NAME} FROM ${GenreToMovieDb.TABLE_NAME}" +
            " WHERE ${GenreToMovieDb.MOVIE_ID_NAME} = :movieId")
    suspend fun getGenresByMovieId(movieId: Int): List<GenreDb>

//    @Query("SELECT * FROM ${GenreDb.TABLE_NAME} WHERE ${GenreDb.ID_NAME} = :id")
//    suspend fun getGenreById(id: Int): GenreDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGenres(genres: List<GenreDb>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun saveGenreToMovies(list: List<GenreToMovieDb>)

}