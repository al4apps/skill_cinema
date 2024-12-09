package com.al4apps.skillcinema.data.models.movie

import com.al4apps.skillcinema.domain.model.MovieDetail
import com.google.gson.annotations.SerializedName

data class FilmDetailDto(
    @SerializedName("kinopoiskId")
    override val kinopoiskId: Int,
    @SerializedName("nameRu")
    override val nameRu: String?,
    @SerializedName("nameEn")
    override val nameEn: String? = null,
    @SerializedName("year")
    override val year: Int,
    @SerializedName("posterUrl")
    override val posterUrl: String,
    @SerializedName("posterUrlPreview")
    override val posterUrlPreview: String,
    @SerializedName("countries")
    override val countries: List<CountryDto>,
    @SerializedName("genres")
    override val genres: List<GenreDto>,
    @SerializedName("duration")
    override val duration: Int,
    @SerializedName("premiereRu")
    override val premiereRu: String? = null,
    @SerializedName("rating")
    override val rating: Float? = null,
    @SerializedName("ratingKinopoisk")
    override val ratingKinopoisk: Float? = null,
    @SerializedName("ratingImdb")
    override val ratingImdb: Float? = null,
    @SerializedName("imdbId")
    override val imdbId: String?,
    @SerializedName("nameOriginal")
    override val nameOriginal: String?,
    @SerializedName("logoUrl")
    override val logoUrl: String?,
    @SerializedName("filmLength")
    override val filmLength: Int,
    @SerializedName("slogan")
    override val slogan: String?,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("shortDescription")
    override val shortDescription: String?,
    @SerializedName("ratingAgeLimits")
    override val ratingAgeLimits: String?,
    @SerializedName("startYear")
    override val startYear: Int?,
    @SerializedName("endYear")
    override val endYear: Int?,
    @SerializedName("serial")
    override val serial: Boolean,
    @SerializedName("completed")
    override val completed: Boolean,
    override var isWatched: Boolean = false,
) : MovieDetail