package com.al4apps.skillcinema.domain

import com.al4apps.skillcinema.domain.model.FilmType
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.model.Order
import com.al4apps.skillcinema.domain.model.QueryPopular
import com.al4apps.skillcinema.domain.model.QuerySeries
import com.al4apps.skillcinema.domain.model.Top250
import kotlin.random.Random
import kotlin.random.nextInt

object ApiSettings {

    private const val COLLECTION_TOP_250 = "TOP_250_MOVIES"
    private const val COLLECTION_POPULAR_MOVIES = "TOP_POPULAR_MOVIES"
    private const val COLLECTION_POPULAR_SERIES = "POPULAR_SERIES"
    const val FIRST_PAGE = 1
    const val PAGE_SIZE = 20
    const val API_DATE_FORMAT = "yyyy-MM-dd"
    const val MOVIE_PREMIERES_RANGE_MILLIS = 1_209_600_000L

    object QueryValues {
        val populars = QueryPopular(COLLECTION_POPULAR_MOVIES)
        val top250 = Top250(COLLECTION_TOP_250)
        val querySeries = QuerySeries(FilmType.TV_SERIES.name, Order.NUM_VOTE)
    }

    fun generateShareLink(kinopoiskId: Int): String {
        return "https://www.kinopoisk.ru/film/$kinopoiskId/"
    }

    fun getRandomGenreCountryFilter(except: GenreCountryFilter?): GenreCountryFilter {
        val randomGenre = filters.keys.random()
        val country = filters[randomGenre]!!
        var filter = GenreCountryFilter(randomGenre, country)
        if (except != null && filter == except) filter = getRandomGenreCountryFilter(except)
        return filter
    }

    object Staff {
        const val PROFESSION_KEY_ACTOR = "ACTOR"
        const val PROFESSION_KEY_DIRECTOR = "DIRECTOR"
        const val PROFESSION_KEY_PRODUCER = "PRODUCER"
        const val PROFESSION_KEY_WRITER = "WRITER"
    }

    object Genres {
        const val THRILLER = 1
        const val DRAMA = 2
        const val CRIME = 3
        const val MELODRAMA = 4
        const val DETECTIVE = 5
        const val FANTASTIC = 6
        const val ADVENTURE = 7
        const val WESTERN = 10
        const val ACTION = 11
        const val FANTASY = 12
        const val COMEDY = 13
        const val HORROR = 17
        const val CARTOON = 18
        const val ANIME = 24
    }

    object Countries {
        const val USA = 1
        const val FRANCE = 3
        const val GREAT_BRITAIN = 5
        const val INDIA = 7
        const val SPAIN = 8
        const val JAPAN = 16
        const val USSR = 33
        const val RUSSIA = 34
        const val SOUTH_KOREA = 49
    }

    private val filters = mapOf(
        Genres.THRILLER to Countries.USA,
        Genres.DRAMA to Countries.FRANCE,
        Genres.CRIME to Countries.RUSSIA,
        Genres.FANTASTIC to Countries.USA,
        Genres.DETECTIVE to Countries.GREAT_BRITAIN,
        Genres.ADVENTURE to Countries.USA,
        Genres.WESTERN to Countries.USA,
        Genres.ACTION to Countries.USA,
        Genres.ACTION to Countries.GREAT_BRITAIN,
        Genres.COMEDY to Countries.FRANCE,
        Genres.COMEDY to Countries.USA,
        Genres.FANTASY to Countries.USA,
        Genres.FANTASY to Countries.GREAT_BRITAIN,
        Genres.HORROR to Countries.USA,
        Genres.CARTOON to Countries.USA,
        Genres.ANIME to Countries.JAPAN,
        Genres.DRAMA to Countries.SPAIN,
        Genres.MELODRAMA to Countries.INDIA,
        Genres.ACTION to Countries.SOUTH_KOREA,
        Genres.COMEDY to Countries.USSR
    )
}