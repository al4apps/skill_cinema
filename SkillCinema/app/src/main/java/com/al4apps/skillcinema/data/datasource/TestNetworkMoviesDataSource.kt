package com.al4apps.skillcinema.data.datasource

import com.al4apps.skillcinema.data.models.movie.FilmDto
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.data.network.KinopoiskApi
import com.al4apps.skillcinema.domain.model.Movie
import com.al4apps.skillcinema.domain.model.QueryPopular
import com.al4apps.skillcinema.domain.model.QueryPremieres
import com.al4apps.skillcinema.domain.model.QuerySearch
import com.al4apps.skillcinema.domain.model.QuerySeries
import com.al4apps.skillcinema.domain.model.Top250
import kotlinx.coroutines.delay
import javax.inject.Inject

private const val MILLIS = 0L
class TestNetworkMoviesDataSource @Inject constructor(
    private val kinopoiskApi: KinopoiskApi
) : MoviesDataSource {
    override suspend fun getMoviePremieres(queryParams: QueryPremieres): List<FilmDto> {
        delay(MILLIS)
        return emptyList()
    }

    override suspend fun getMoviesPopulars(queryParams: QueryPopular, page: Int): List<FilmDto> {
        delay(MILLIS)
        return emptyList()
    }

    override suspend fun getMoviesTop250(queryParams: Top250, page: Int): List<FilmDto> {
        delay(MILLIS)
        return emptyList()
    }

    override suspend fun getSeries(queryParams: QuerySeries, page: Int): List<FilmDto> {
        delay(MILLIS)
        return emptyList()
    }

    override suspend fun getDynamicFilms(queryParams: GenreCountryFilter, page: Int): List<FilmDto> {
        delay(MILLIS)
        return emptyList()
    }

    override suspend fun getFilmsByParams(queryParams: QuerySearch, page: Int): List<Movie> {
        delay(MILLIS)
        return emptyList()
    }
}