package com.al4apps.skillcinema.data.models.movie

import com.al4apps.skillcinema.domain.model.SimilarFilm
import com.google.gson.annotations.SerializedName

data class SimilarFilmDto(
    @SerializedName("filmId")
    override val filmId: Int,
    @SerializedName("nameRu")
    override val nameRu: String,
    @SerializedName("nameEn")
    override val nameEn: String? = null,
    @SerializedName("nameOriginal")
    override val nameOriginal: String?,
    @SerializedName("posterUrl")
    override val posterUrl: String,
    @SerializedName("posterUrlPreview")
    override val posterUrlPreview: String,
    @SerializedName("relationType")
    override val relationType: String
) : SimilarFilm
