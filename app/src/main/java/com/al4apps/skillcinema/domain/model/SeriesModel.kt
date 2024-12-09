package com.al4apps.skillcinema.domain.model

class SeriesModel(
    override val seasons: List<SeasonModel>
) : Series

class SeasonModel(
    override val number: Int,
    override val episodes: List<EpisodeModel>
) : Season, SeriesListModel()

class EpisodeModel(
    override val seasonNumber: Int,
    override val episodeNumber: Int,
    override val nameRu: String?,
    override val nameEn: String?,
    override val synopsis: String?,
    override val releaseDate: String?
) : Episode, SeriesListModel()

class SeriesInfoModel(
    val seasonsCount: Int,
    val episodesCount: Int
)

sealed class SeriesListModel