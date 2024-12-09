package com.al4apps.skillcinema.domain.repository

import com.al4apps.skillcinema.domain.model.ImageModel
import com.al4apps.skillcinema.domain.model.QueryImages
import com.al4apps.skillcinema.domain.model.MovieDetailModel
import com.al4apps.skillcinema.domain.model.SeriesModel
import com.al4apps.skillcinema.domain.model.SimilarFilmModel

interface MovieInfoRepository {
    suspend fun getMovieInfo(kinopoiskId: Int): MovieDetailModel

    suspend fun getSimilarFilmsById(kinopoiskId: Int): List<SimilarFilmModel>

    suspend fun getFilmImages(queryParams: QueryImages, page: Int): List<ImageModel>

    suspend fun getSeriesInfo(kinopoiskId: Int): SeriesModel
}