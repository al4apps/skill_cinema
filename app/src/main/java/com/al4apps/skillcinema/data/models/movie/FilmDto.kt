package com.al4apps.skillcinema.data.models.movie

import com.al4apps.skillcinema.domain.model.Country
import com.al4apps.skillcinema.domain.model.Genre
import com.al4apps.skillcinema.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class FilmDto(
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
) : Movie

data class GenreDto(
    @SerializedName("genre")
    override val genre: String
): Genre

data class CountryDto(
    @SerializedName("country")
    override val country: String
): Country
