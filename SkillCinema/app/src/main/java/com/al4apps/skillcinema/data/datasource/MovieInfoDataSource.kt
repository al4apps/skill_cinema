package com.al4apps.skillcinema.data.datasource

import com.al4apps.skillcinema.data.models.movie.FilmDetailDto
import com.al4apps.skillcinema.data.models.movie.ImageDto
import com.al4apps.skillcinema.data.models.movie.SeriesDto
import com.al4apps.skillcinema.data.models.movie.SimilarFilmDto
import com.al4apps.skillcinema.domain.model.QueryImages

interface MovieInfoDataSource {
    suspend fun getMovieDetailInfo(kinopoiskId: Int): FilmDetailDto

    suspend fun getSimilarFilmsById(kinopoiskId: Int): List<SimilarFilmDto>

    suspend fun getFilmImages(queryParams: QueryImages): List<ImageDto>

    suspend fun getSeriesInfo(kinopoiskId: Int): SeriesDto

}