package com.al4apps.skillcinema.data.network.responsemodels

import com.al4apps.skillcinema.data.models.movie.FilmDto
import com.google.gson.annotations.SerializedName

class PopularsResponseModel(
    @SerializedName("pagesCount")
    val pagesCount: Int,
    @SerializedName("films")
    val films: List<FilmDto>
)