package com.al4apps.skillcinema.domain.model

sealed class QueryParams

data class QuerySearch(
    val genreId: Int?,
    val countryId: Int?,
    val order: Order,
    val type: FilmType,
    val ratingFrom: Int,
    val ratingTo: Int,
    val yearFrom: Int?,
    val yearTo: Int?,
    val keyword: String
) : QueryParams()

class QueryPremieres(
    val year: Int, val month: QueryMonth
) : QueryParams()

class QueryPopular(
    val type: String,
) : QueryParams()

class Top250(
    val type: String
) : QueryParams()

class QuerySeries(
    val type: String, val order: Order
) : QueryParams()

class GenreCountryFilter(
    val genreId: Int, val countryId: Int, val order: Order = Order.NUM_VOTE
) : QueryParams()

class QueryImages(
    val kinopoiskId: Int, val type: ImageType
) : QueryParams()

enum class Order {
    RATING, NUM_VOTE, YEAR
}

enum class FilmType {
    FILM, TV_SHOW, TV_SERIES, MINI_SERIES, ALL
}

enum class ImageType(val queryText: String) {
    STILL("STILL"), SHOOTING("SHOOTING"), POSTER("POSTER"), FAN_ART("FAN_ART"), PROMO("PROMO"), CONCEPT(
        "CONCEPT"
    ),
    WALLPAPER("WALLPAPER"), COVER("COVER"), SCREENSHOT("SCREENSHOT"),
}

enum class QueryMonth(val nameString: String, val number: Int, var days: Int) {
    JANUARY("JANUARY", 1, 31),
    FEBRUARY("FEBRUARY", 2, 28),
    MARCH("MARCH", 3, 31),
    APRIL("APRIL", 4, 30),
    MAY("MAY", 5, 31),
    JUNE("JUNE", 6, 30),
    JULY("JULY", 7, 31),
    AUGUST("AUGUST", 8, 31),
    SEPTEMBER("SEPTEMBER", 9, 30),
    OCTOBER("OCTOBER", 10, 31),
    NOVEMBER("NOVEMBER", 11, 30),
    DECEMBER("DECEMBER", 12, 31)
}

fun getQueryMonthFromMonthNumber(number: Int): QueryMonth {
    return when (number) {
        1 -> QueryMonth.JANUARY
        2 -> QueryMonth.FEBRUARY
        3 -> QueryMonth.MARCH
        4 -> QueryMonth.APRIL
        5 -> QueryMonth.MAY
        6 -> QueryMonth.JUNE
        7 -> QueryMonth.JULY
        8 -> QueryMonth.AUGUST
        9 -> QueryMonth.SEPTEMBER
        10 -> QueryMonth.OCTOBER
        11 -> QueryMonth.NOVEMBER
        12 -> QueryMonth.DECEMBER
        else -> QueryMonth.JANUARY
    }
}

fun QueryMonth.getNextMonth(
): QueryMonth {
    return if (this == QueryMonth.DECEMBER) QueryMonth.JANUARY
    else getQueryMonthFromMonthNumber(this.number + 1)
}
