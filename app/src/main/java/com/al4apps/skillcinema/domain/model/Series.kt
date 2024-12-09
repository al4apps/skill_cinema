package com.al4apps.skillcinema.domain.model

interface Series {
    val seasons: List<Season>
}

interface Season {
    val number: Int
    val episodes: List<Episode>
}

interface Episode {
    val seasonNumber: Int
    val episodeNumber: Int
    val nameRu: String?
    val nameEn: String?
    val synopsis: String?
    val releaseDate: String?
}