package com.al4apps.skillcinema.data.models.staff

import com.al4apps.skillcinema.domain.model.Staff
import com.google.gson.annotations.SerializedName

data class StaffDto(
    @SerializedName("staffId")
    override val staffId: Int,
    @SerializedName("nameRu")
    override val nameRu: String,
    @SerializedName("nameEn")
    override val nameEn: String,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("posterUrl")
    override val posterUrl: String,
    @SerializedName("professionText")
    override val professionText: String,
    @SerializedName("professionKey")
    override val professionKey: String
) : Staff
