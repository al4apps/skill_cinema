package com.al4apps.skillcinema.data.datasource

import com.al4apps.skillcinema.data.models.movie.FilmDto
import com.al4apps.skillcinema.data.network.KinopoiskApi
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.model.Movie
import com.al4apps.skillcinema.domain.model.QueryPopular
import com.al4apps.skillcinema.domain.model.QueryPremieres
import com.al4apps.skillcinema.domain.model.QuerySearch
import com.al4apps.skillcinema.domain.model.QuerySeries
import com.al4apps.skillcinema.domain.model.Top250
import javax.inject.Inject

class NetworkMoviesDataSourceImpl @Inject constructor(
    private val kinopoiskApi: KinopoiskApi
) : MoviesDataSource {
    override suspend fun getMoviePremieres(queryParams: QueryPremieres): List<FilmDto> {
        return kinopoiskApi.getPremieres(
            year = queryParams.year,
            month = queryParams.month.nameString
        ).items
    }

    override suspend fun getMoviesPopulars(queryParams: QueryPopular, page: Int): List<FilmDto> {
        return kinopoiskApi.getMoviesCollection(queryParams.type, page).items
    }

    override suspend fun getMoviesTop250(queryParams: Top250, page: Int): List<FilmDto> {
        return kinopoiskApi.getMoviesCollection(queryParams.type, page).items
    }

    override suspend fun getSeries(queryParams: QuerySeries, page: Int): List<FilmDto> {
        return kinopoiskApi.getPopularSeries(
            order = queryParams.order.name,
            type = queryParams.type,
            page
        ).items
    }

    override suspend fun getDynamicFilms(
        queryParams: GenreCountryFilter,
        page: Int
    ): List<FilmDto> {
        return kinopoiskApi.getFilmsByGenreCountry(
            queryParams.countryId,
            queryParams.genreId,
            queryParams.order.name,
            page
        ).items
    }

    override suspend fun getFilmsByParams(queryParams: QuerySearch, page: Int): List<FilmDto> {
        return kinopoiskApi.getFilms(
            queryParams.countryId,
            queryParams.genreId,
            queryParams.order.name,
            queryParams.type.name,
            queryParams.ratingFrom,
            queryParams.ratingTo,
            queryParams.yearFrom,
            queryParams.yearTo,
            queryParams.keyword,
            page
        ).items
    }
}