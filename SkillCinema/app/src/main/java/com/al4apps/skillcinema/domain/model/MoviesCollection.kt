package com.al4apps.skillcinema.domain.model

import androidx.annotation.StringRes
import com.al4apps.skillcinema.R

class MoviesCollection(
    val info: CollectionInfo,
    val movies: List<HomeListModel>,
)

class CollectionInfo(
    val type: MovieCollectionType,
    @StringRes
    val title: Int = type.title,
    val dynamicCollectionInfo: DynamicCollectionInfo? = null
)

enum class MovieCollectionType(@StringRes val title: Int) {
    PREMIER(R.string.movie_type_premieres_title),
    POPULAR(R.string.movie_type_populars_title),
    TOP250(R.string.movie_type_top250_title),
    SERIES(R.string.movie_type_series_title),
    DYNAMIC(R.string.movie_type_dynamic_title),
    SIMILAR(R.string.movie_type_similar_title),
    WITH_PERSON(R.string.movie_type_with_person_title),
    FROM_DB_WATCHED(R.string.movie_type_from_db_watched_title),
    FROM_DB_VIEWS(R.string.movie_type_from_db_interesting_title)
}

class DynamicCollectionInfo(
    @StringRes val genreRes: Int,
    @StringRes val countryRes: Int,
    val genreId: Int,
    val countryId: Int,
)