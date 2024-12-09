package com.al4apps.skillcinema.data.repository

import com.al4apps.skillcinema.data.datasource.MovieInfoDataSourceImpl
import com.al4apps.skillcinema.data.models.movie.EpisodeDto
import com.al4apps.skillcinema.data.models.movie.FilmDetailDto
import com.al4apps.skillcinema.data.models.movie.SeriesDto
import com.al4apps.skillcinema.data.models.movie.SimilarFilmDto
import com.al4apps.skillcinema.domain.model.CountryModel
import com.al4apps.skillcinema.domain.model.EpisodeModel
import com.al4apps.skillcinema.domain.model.GenreModel
import com.al4apps.skillcinema.domain.model.ImageModel
import com.al4apps.skillcinema.domain.model.QueryImages
import com.al4apps.skillcinema.domain.model.MovieDetailModel
import com.al4apps.skillcinema.domain.model.SeasonModel
import com.al4apps.skillcinema.domain.model.SeriesModel
import com.al4apps.skillcinema.domain.model.SimilarFilmModel
import com.al4apps.skillcinema.domain.repository.MovieInfoRepository
import com.al4apps.skillcinema.domain.toImageModel
import javax.inject.Inject

class MovieInfoRepositoryImpl @Inject constructor(
    private val dataSource: MovieInfoDataSourceImpl
) : MovieInfoRepository {
    override suspend fun getMovieInfo(kinopoiskId: Int): MovieDetailModel {
        return dataSource.getMovieDetailInfo(kinopoiskId).toMovieDetailModel()
    }

    override suspend fun getSimilarFilmsById(kinopoiskId: Int): List<SimilarFilmModel> {
        return dataSource.getSimilarFilmsById(kinopoiskId).map { it.toSimilarFilmModel() }
    }

    override suspend fun getFilmImages(queryParams: QueryImages, page: Int): List<ImageModel> {
        return dataSource.getFilmImages(queryParams).map { it.toImageModel() }
    }

    override suspend fun getSeriesInfo(kinopoiskId: Int): SeriesModel {
        return dataSource.getSeriesInfo(kinopoiskId).toSeriesModel()
    }

    private fun SeriesDto.toSeriesModel(): SeriesModel {
        return SeriesModel(seasons = seasons.map { seasonDto ->
            SeasonModel(seasonDto.number, seasonDto.episodes.map { episodeDto ->
                episodeDto.toEpisodeModel()
            })
        })
    }

    private fun EpisodeDto.toEpisodeModel(): EpisodeModel {
        return EpisodeModel(
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            nameRu = nameRu,
            nameEn = nameEn,
            synopsis = synopsis,
            releaseDate = releaseDate
        )
    }

    private fun SimilarFilmDto.toSimilarFilmModel(): SimilarFilmModel {
        return SimilarFilmModel(
            filmId = filmId,
            nameRu = nameRu,
            nameEn = nameEn,
            nameOriginal = nameOriginal,
            posterUrl = posterUrl,
            posterUrlPreview = posterUrlPreview,
            relationType = relationType
        )
    }

    private fun FilmDetailDto.toMovieDetailModel(): MovieDetailModel {
        return MovieDetailModel(
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
            ratingImdb = ratingImdb,
            imdbId = imdbId,
            nameOriginal = nameOriginal,
            logoUrl = logoUrl,
            filmLength = filmLength,
            slogan = slogan,
            description = description,
            shortDescription = shortDescription,
            ratingAgeLimits = ratingAgeLimits,
            startYear = startYear,
            endYear = endYear,
            serial = serial,
            completed = completed
        )
    }
}