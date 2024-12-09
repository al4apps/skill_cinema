package com.al4apps.skillcinema.data.repository

import com.al4apps.skillcinema.data.datasource.NetworkMoviesDataSourceImpl
import com.al4apps.skillcinema.data.models.movie.FilmDto
import com.al4apps.skillcinema.domain.model.CountryModel
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.model.GenreModel
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.model.QueryPopular
import com.al4apps.skillcinema.domain.model.QueryPremieres
import com.al4apps.skillcinema.domain.model.QuerySearch
import com.al4apps.skillcinema.domain.model.QuerySeries
import com.al4apps.skillcinema.domain.model.Top250
import com.al4apps.skillcinema.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val dataSource: NetworkMoviesDataSourceImpl,
) : MoviesRepository {
    override suspend fun getMoviePremieres(queryParams: QueryPremieres): List<MovieModel> {
        return dataSource.getMoviePremieres(queryParams).map { it.toMovieModel() }
    }

    override suspend fun getPopularMovies(queryParams: QueryPopular, page: Int): List<MovieModel> {
        return dataSource.getMoviesPopulars(queryParams, page).map { it.toMovieModel() }
    }

    override suspend fun getMoviesTop250(queryParams: Top250, page: Int): List<MovieModel> {
        return dataSource.getMoviesTop250(queryParams, page).map { it.toMovieModel() }
    }

    override suspend fun getSeries(queryParams: QuerySeries, page: Int): List<MovieModel> {
        return dataSource.getSeries(queryParams, page).map { it.toMovieModel() }
    }

    override suspend fun getDynamicMoviesCollection(
        queryParams: GenreCountryFilter,
        page: Int
    ): List<MovieModel> {
        return dataSource.getDynamicFilms(queryParams, page).map { it.toMovieModel() }
    }

    override suspend fun getFilmsByParams(queryParams: QuerySearch, page: Int): List<MovieModel> {
        return dataSource.getFilmsByParams(queryParams, page).map { it.toMovieModel() }
    }

    private fun FilmDto.toMovieModel(): MovieModel {
        return MovieModel(
            kinopoiskId = kinopoiskId,
            nameRu = nameRu,
            nameEn = nameEn,
            year = year,
            posterUrl = posterUrl,
            posterUrlPreview = posterUrlPreview,
            countries = countries.map { CountryModel(it.country) },
            genres = genres.map { GenreModel(it.genre) },
            duration = duration,
            premiereRu = premiereRu,
            rating = rating,
            ratingKinopoisk = ratingKinopoisk,
            ratingImdb = ratingImdb
        )
    }


}