package com.al4apps.skillcinema.data.datasource

import com.al4apps.skillcinema.domain.model.Movie
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.model.QueryPopular
import com.al4apps.skillcinema.domain.model.QueryPremieres
import com.al4apps.skillcinema.domain.model.QuerySearch
import com.al4apps.skillcinema.domain.model.QuerySeries
import com.al4apps.skillcinema.domain.model.Top250

interface MoviesDataSource {

    suspend fun getMoviePremieres(queryParams: QueryPremieres): List<Movie>
    suspend fun getMoviesPopulars(queryParams: QueryPopular, page: Int): List<Movie>
    suspend fun getMoviesTop250(queryParams: Top250, page: Int): List<Movie>
    suspend fun getSeries(queryParams: QuerySeries, page: Int): List<Movie>
    suspend fun getDynamicFilms(queryParams: GenreCountryFilter, page: Int): List<Movie>
    suspend fun getFilmsByParams(queryParams: QuerySearch, page: Int): List<Movie>
}