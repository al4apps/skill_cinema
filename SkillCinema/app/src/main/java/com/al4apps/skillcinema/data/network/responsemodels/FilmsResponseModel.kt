package com.al4apps.skillcinema.data.network.responsemodels

import com.al4apps.skillcinema.data.models.movie.FilmDto
import com.google.gson.annotations.SerializedName

class FilmsResponseModel(
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalPages")
    val totalPages: Int,
    @SerializedName("items")
    val items: List<FilmDto>
)