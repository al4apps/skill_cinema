package com.al4apps.skillcinema.domain

import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.data.models.movie.ImageDto
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.model.CollectionInfo
import com.al4apps.skillcinema.domain.model.DynamicCollectionInfo
import com.al4apps.skillcinema.domain.model.FooterModel
import com.al4apps.skillcinema.domain.model.HomeListModel
import com.al4apps.skillcinema.domain.model.ImageModel
import com.al4apps.skillcinema.domain.model.MovieCollectionType
import com.al4apps.skillcinema.domain.model.MovieModel

fun List<MovieModel>.addFooter(footerModel: FooterModel): List<HomeListModel> {
    return if (this.isNotEmpty()) this + footerModel
    else this
}

fun ImageDto.toImageModel(): ImageModel {
    return ImageModel(imageUrl, previewUrl)
}

fun GenreCountryFilter.toCollectionInfo(): CollectionInfo {
    val genreStringRes = when (genreId) {
        ApiSettings.Genres.THRILLER -> R.string.movie_genre_thrillers
        ApiSettings.Genres.DRAMA -> R.string.movie_genre_dramas
        ApiSettings.Genres.CRIME -> R.string.movie_genre_crimes
        ApiSettings.Genres.MELODRAMA -> R.string.movie_genre_melodramas
        ApiSettings.Genres.DETECTIVE -> R.string.movie_genre_detectives
        ApiSettings.Genres.FANTASTIC -> R.string.movie_genre_fantastic
        ApiSettings.Genres.ADVENTURE -> R.string.movie_genre_adventures
        ApiSettings.Genres.WESTERN -> R.string.movie_genre_westerns
        ApiSettings.Genres.ACTION -> R.string.movie_genre_action
        ApiSettings.Genres.FANTASY -> R.string.movie_genre_fantasy
        ApiSettings.Genres.COMEDY -> R.string.movie_genre_comedies
        ApiSettings.Genres.HORROR -> R.string.movie_genre_horrors
        ApiSettings.Genres.CARTOON -> R.string.movie_genre_cartoons
        ApiSettings.Genres.ANIME -> R.string.movie_genre_anime
        else -> R.string.movie_genre_basic
    }
    val countryStringRes = when (countryId) {
        ApiSettings.Countries.USA -> R.string.movie_country_usa
        ApiSettings.Countries.FRANCE -> R.string.movie_country_france
        ApiSettings.Countries.GREAT_BRITAIN -> R.string.movie_country_great_britain
        ApiSettings.Countries.INDIA -> R.string.movie_country_india
        ApiSettings.Countries.SPAIN -> R.string.movie_country_spain
        ApiSettings.Countries.JAPAN -> R.string.movie_country_japan
        ApiSettings.Countries.USSR -> R.string.movie_country_ussr
        ApiSettings.Countries.RUSSIA -> R.string.movie_country_russia
        ApiSettings.Countries.SOUTH_KOREA -> R.string.movie_country_south_korea
        else -> R.string.movie_country_empty
    }
    return CollectionInfo(
        type = MovieCollectionType.DYNAMIC,
        dynamicCollectionInfo = DynamicCollectionInfo(
            genreStringRes,
            countryStringRes,
            genreId,
            countryId,
        )
    )
}