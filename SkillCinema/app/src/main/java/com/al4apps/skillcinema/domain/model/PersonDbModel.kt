package com.al4apps.skillcinema.domain.model

data class PersonDbModel(
    val id: Int,
    val nameRu: String?,
    val nameEn: String?,
    val posterUrl: String,
    val profession: String?,
    val timestamp: Long
) : MoviesAndPersonsDb(timestamp)
