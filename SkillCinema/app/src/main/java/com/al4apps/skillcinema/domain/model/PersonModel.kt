package com.al4apps.skillcinema.domain.model

data class PersonModel(
    override val personId: Int,
    override val nameRu: String?,
    override val nameEn: String?,
    override val sex: Sex?,
    override val posterUrl: String,
    override val profession: String?,
    override val films: List<FilmWithPersonModel>
) : Person

data class FilmWithPersonModel(
    override val filmId: Int,
    override val nameRu: String?,
    override val nameEn: String?,
    override val rating: Float?,
    override val general: Boolean,
    override val description: String?,
    override val professionKey: ProfessionKey?
) : FilmWithPerson, HomeListModel()
