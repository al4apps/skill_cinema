package com.al4apps.skillcinema.domain.model

data class MovieModel(
    override val kinopoiskId: Int,
    override val nameRu: String? = "",
    override val nameEn: String?,
    override val year: Int,
    override val posterUrl: String,
    override val posterUrlPreview: String,
    override val countries: List<CountryModel>,
    override val genres: List<GenreModel>,
    override val duration: Int,
    override val premiereRu: String? = null,
    override val rating: Float? = null,
    override val ratingKinopoisk: Float?,
    override val ratingImdb: Float?,
    var isWatched: Boolean = false
) : Movie, HomeListModel()

data class GenreModel(
    override val genre: String
) : Genre

data class CountryModel(
    override val country: String
) : Country