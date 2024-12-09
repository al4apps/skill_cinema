package com.al4apps.skillcinema.data.network.responsemodels

import com.al4apps.skillcinema.data.models.movie.FilmDto
import com.google.gson.annotations.SerializedName

class PremieresResponseModel(
    @SerializedName("items")
    val items: List<FilmDto>,
    @SerializedName("total")
    val total: Int
)