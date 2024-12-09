package com.al4apps.skillcinema.domain.model

data class MovieDbModel(
    val kinopoiskId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val posterUrl: String,
    val ratingKinopoisk: Float?,
    val genres: List<GenreModel>,
    val timestamp: Long,
    var isWatched: Boolean = false
) : MoviesAndPersonsDb(timestamp)