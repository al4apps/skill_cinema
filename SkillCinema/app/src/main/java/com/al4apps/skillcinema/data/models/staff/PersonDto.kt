package com.al4apps.skillcinema.data.models.staff

import com.al4apps.skillcinema.domain.model.FilmWithPerson
import com.al4apps.skillcinema.domain.model.Person
import com.al4apps.skillcinema.domain.model.ProfessionKey
import com.al4apps.skillcinema.domain.model.Sex
import com.google.gson.annotations.SerializedName

data class PersonDto(
    @SerializedName("personId")
    override val personId: Int,
    @SerializedName("nameRu")
    override val nameRu: String?,
    @SerializedName("nameEn")
    override val nameEn: String?,
    @SerializedName("sex")
    override val sex: Sex?,
    @SerializedName("posterUrl")
    override val posterUrl: String,
    @SerializedName("profession")
    override val profession: String?,
    @SerializedName("films")
    override val films: List<FilmWithPersonDto>
) : Person

data class FilmWithPersonDto(
    @SerializedName("filmId")
    override val filmId: Int,
    @SerializedName("nameRu")
    override val nameRu: String?,
    @SerializedName("nameEn")
    override val nameEn: String?,
    @SerializedName("rating")
    override val rating: Float?,
    @SerializedName("general")
    override val general: Boolean,
    @SerializedName("description")
    override val description: String?,
    @SerializedName("professionKey")
    override val professionKey: ProfessionKey?
) : FilmWithPerson
