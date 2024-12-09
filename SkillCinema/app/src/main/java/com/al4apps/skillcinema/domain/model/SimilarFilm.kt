package com.al4apps.skillcinema.domain.model

interface SimilarFilm {
    val filmId: Int
    val nameRu: String
    val nameEn: String?
    val nameOriginal: String?
    val posterUrl: String
    val posterUrlPreview: String
    val relationType: String
}