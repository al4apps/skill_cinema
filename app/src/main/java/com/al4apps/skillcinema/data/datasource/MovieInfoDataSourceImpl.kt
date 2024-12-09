package com.al4apps.skillcinema.data.datasource

import com.al4apps.skillcinema.data.models.movie.FilmDetailDto
import com.al4apps.skillcinema.data.models.movie.ImageDto
import com.al4apps.skillcinema.data.models.movie.SeriesDto
import com.al4apps.skillcinema.data.models.movie.SimilarFilmDto
import com.al4apps.skillcinema.data.network.KinopoiskApi
import com.al4apps.skillcinema.domain.model.QueryImages
import javax.inject.Inject

class MovieInfoDataSourceImpl @Inject constructor(
    private val kinopoiskApi: KinopoiskApi
) : MovieInfoDataSource {
    override suspend fun getMovieDetailInfo(kinopoiskId: Int): FilmDetailDto {
        return kinopoiskApi.getMovieDetailInfo(kinopoiskId)
    }

    override suspend fun getSimilarFilmsById(kinopoiskId: Int): List<SimilarFilmDto> {
        return kinopoiskApi.getFilmSimilars(kinopoiskId).items
    }

    override suspend fun getFilmImages(queryParams: QueryImages): List<ImageDto> {
        return kinopoiskApi.getFilmImages(queryParams.kinopoiskId, queryParams.type.queryText).items
    }

    override suspend fun getSeriesInfo(kinopoiskId: Int): SeriesDto {
        return kinopoiskApi.getSeasonsInfo(kinopoiskId)
    }
}