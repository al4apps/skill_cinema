package com.al4apps.skillcinema.data.network.responsemodels

import com.al4apps.skillcinema.data.models.movie.SimilarFilmDto
import com.google.gson.annotations.SerializedName

class SimilarResponseModel(
    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: List<SimilarFilmDto>
)