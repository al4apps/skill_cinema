package com.al4apps.skillcinema.presentation.search

import com.al4apps.skillcinema.presentation.searchsettings.MovieType
import com.al4apps.skillcinema.presentation.searchsettings.RatingRange
import com.al4apps.skillcinema.presentation.searchsettings.SearchParams
import com.al4apps.skillcinema.presentation.searchsettings.SortType
import com.al4apps.skillcinema.presentation.searchsettings.years.YearsRange

object BasicSearchParams {
    const val RATING_MIN = 0f
    const val RATING_MAX = 10f

    private val anyRatingRange = RatingRange(RATING_MIN, RATING_MAX)
    private val anyYear = YearsRange(null, null)

    val basicSearchParams = SearchParams(
        movieType = MovieType.ALL,
        country = null,
        genre = null,
        yearsRange = anyYear,
        ratingRange = anyRatingRange,
        sortType = SortType.POPULARITY,
        hideViewed = false
    )


}