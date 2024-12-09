package com.al4apps.skillcinema.domain.model

interface Movie {
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
}

interface Genre {
    val genre: String
}

interface Country {
    val country: String
}