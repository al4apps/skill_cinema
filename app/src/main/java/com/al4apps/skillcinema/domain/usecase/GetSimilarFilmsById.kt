package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MovieInfoRepositoryImpl
import com.al4apps.skillcinema.domain.model.MovieDetailModel
import com.al4apps.skillcinema.domain.model.MovieModel
import javax.inject.Inject

class GetSimilarFilmsById @Inject constructor(
    private val movieInfoRepository: MovieInfoRepositoryImpl
) {
    suspend fun execute(kinopoiskId: Int): List<MovieModel> {
        val films = movieInfoRepository.getSimilarFilmsById(kinopoiskId).map {
            movieInfoRepository.getMovieInfo(it.filmId).toMovieModel()
        }
        return films
    }

    private fun MovieDetailModel.toMovieModel(): MovieModel {
        return MovieModel(
            kinopoiskId = kinopoiskId,
            nameRu = nameRu,
            nameEn = nameEn,
            year = year,
            posterUrl = posterUrl,
            posterUrlPreview = posterUrlPreview,
            countries = countries,
            genres = genres,
            duration = duration,
            premiereRu = premiereRu,
            rating = rating,
            ratingKinopoisk = ratingKinopoisk,
            ratingImdb = ratingImdb
        )
    }
}