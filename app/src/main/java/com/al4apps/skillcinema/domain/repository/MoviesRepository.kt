package com.al4apps.skillcinema.domain.repository

import com.al4apps.skillcinema.data.dbmodels.GenreDb
import com.al4apps.skillcinema.data.dbmodels.MovieDb
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.model.MovieDbModel
import com.al4apps.skillcinema.domain.model.QueryPopular
import com.al4apps.skillcinema.domain.model.QueryPremieres
import com.al4apps.skillcinema.domain.model.Top250
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.model.QuerySearch
import com.al4apps.skillcinema.domain.model.QuerySeries
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getMoviePremieres(queryParams: QueryPremieres): List<MovieModel>

    suspend fun getPopularMovies(queryParams: QueryPopular, page: Int): List<MovieModel>

    suspend fun getMoviesTop250(queryParams: Top250, page: Int): List<MovieModel>

    suspend fun getSeries(queryParams: QuerySeries, page: Int): List<MovieModel>

    suspend fun getDynamicMoviesCollection(queryParams: GenreCountryFilter, page: Int): List<MovieModel>

    suspend fun getFilmsByParams(queryParams: QuerySearch, page: Int): List<MovieModel>
}