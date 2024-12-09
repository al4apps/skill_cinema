package com.al4apps.skillcinema.data.models.movie

import com.al4apps.skillcinema.domain.model.Image
import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("imageUrl")
    override val imageUrl: String,
    @SerializedName("previewUrl")
    override val previewUrl: String,
) : Image