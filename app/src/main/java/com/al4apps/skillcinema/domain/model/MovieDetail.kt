package com.al4apps.skillcinema.domain.model

interface MovieDetail {
    val kinopoiskId: Int
    val nameRu: String?
    val nameEn: String?
    val year: Int
    val posterUrl: String
    val posterUrlPreview: String
    val countries: List<Country>
    val genres: List<Genre>
    val duration: Int
    val premiereRu: String?
    val rating: Float?
    val ratingKinopoisk: Float?
    val ratingImdb: Float?
    val imdbId: String?
    val nameOriginal: String?
    val logoUrl: String?
    val filmLength: Int
    val slogan: String?
    val description: String?
    val shortDescription: String?
    val ratingAgeLimits: String?
    val startYear: Int?
    val endYear: Int?
    val serial: Boolean
    val completed: Boolean
    var isWatched: Boolean
}