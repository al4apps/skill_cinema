package com.al4apps.skillcinema.domain.model

data class MoviesAndPersonsCollection(
    val collectionInfo: CollectionInfo,
    val list: List<MoviesAndPersonsDb>
)

sealed class MoviesAndPersonsDb(
    val time: Long
)
