package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.domain.model.MovieDetailModel
import com.al4apps.skillcinema.domain.model.MovieModel
import javax.inject.Inject

class GetFilmsListByIdsUseCase @Inject constructor(
    private val getMovieInfoUseCase: GetMovieInfoUseCase
) {
    suspend fun execute(list: List<Int>): List<MovieModel> {
        return list.map { getMovieInfoUseCase.execute(it) }
            .map { it.movieDetail }
            .map { it.toMovieModel() }
    }

    private fun MovieDetailModel.toMovieModel(): MovieModel {
        return MovieModel(
            kinopoiskId,
            nameRu,
            nameEn,
            year,
            posterUrl,
            posterUrlPreview,
            countries,
            genres,
            duration,
            premiereRu,
            rating,
            ratingKinopoisk,
            ratingImdb
        )
    }
}