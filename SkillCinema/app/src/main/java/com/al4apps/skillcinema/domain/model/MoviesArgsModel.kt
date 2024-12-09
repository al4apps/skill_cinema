package com.al4apps.skillcinema.domain.model

class MoviesArgsModel(
    val kinopoiskId: Int,
    val genreCountry: GenreCountryFilter,
    val type: MovieCollectionType
)