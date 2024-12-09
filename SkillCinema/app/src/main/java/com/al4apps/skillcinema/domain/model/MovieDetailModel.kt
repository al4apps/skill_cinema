package com.al4apps.skillcinema.domain.model

data class MovieDetailModel(
    override val kinopoiskId: Int,
    override val nameRu: String?,
    override val nameEn: String?,
    override val year: Int,
    override val posterUrl: String,
    override val posterUrlPreview: String,
    override val countries: List<CountryModel>,
    override val genres: List<GenreModel>,
    override val duration: Int,
    override val premiereRu: String?,
    override val rating: Float?,
    override val ratingKinopoisk: Float?,
    override val ratingImdb: Float?,
    override val imdbId: String?,
    override val nameOriginal: String?,
    override val logoUrl: String?,
    override val filmLength: Int,
    override val slogan: String?,
    override val description: String?,
    override val shortDescription: String?,
    override val ratingAgeLimits: String?,
    override val startYear: Int?,
    override val endYear: Int?,
    override val serial: Boolean,
    override val completed: Boolean,
    override var isWatched: Boolean = false
) : MovieDetail