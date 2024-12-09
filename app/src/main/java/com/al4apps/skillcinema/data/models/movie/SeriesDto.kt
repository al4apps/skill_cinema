package com.al4apps.skillcinema.data.models.movie

import com.al4apps.skillcinema.domain.model.Episode
import com.al4apps.skillcinema.domain.model.Season
import com.al4apps.skillcinema.domain.model.Series
import com.google.gson.annotations.SerializedName

class SeriesDto(
    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    override val seasons: List<SeasonDto>,
) : Series

class SeasonDto(
    @SerializedName("number")
    override val number: Int,
    @SerializedName("episodes")
    override val episodes: List<EpisodeDto>
) : Season

class EpisodeDto(
    @SerializedName("seasonNumber")
    override val seasonNumber: Int,
    @SerializedName("episodeNumber")
    override val episodeNumber: Int,
    @SerializedName("nameRu")
    override val nameRu: String?,
    @SerializedName("nameEn")
    override val nameEn: String?,
    @SerializedName("synopsis")
    override val synopsis: String?,
    @SerializedName("releaseDate")
    override val releaseDate: String?
) : Episode