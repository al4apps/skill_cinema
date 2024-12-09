package com.al4apps.skillcinema.presentation.searchsettings

import com.al4apps.skillcinema.presentation.searchsettings.years.YearsRange

data class SearchParams(
    val movieType: MovieType,
    val country: CountryUi?,
    val genre: GenreUi?,
    val yearsRange: YearsRange,
    val ratingRange: RatingRange,
    val sortType: SortType,
    val hideViewed: Boolean
)

sealed class GenreOrCountryUi(
    val name: String,
    val number: Int,
    var isSelected: Boolean
)

enum class MovieType {
    ALL,
    FILM,
    SERIES
}

data class CountryUi(
    val countryName: String,
    val countryNumber: Int,
    var isSelectedCountry: Boolean = false
) : GenreOrCountryUi(countryName, countryNumber, isSelectedCountry)

data class GenreUi(
    val genreName: String,
    val genreNumber: Int,
    var isSelectedGenre: Boolean = false
) : GenreOrCountryUi(genreName, genreNumber, isSelectedGenre)

data class RatingRange(
    val min: Float,
    val max: Float
)

enum class SortType {
    DATE,
    POPULARITY,
    RATING
}