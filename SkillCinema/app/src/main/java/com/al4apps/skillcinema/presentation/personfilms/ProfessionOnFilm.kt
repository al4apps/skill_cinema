package com.al4apps.skillcinema.presentation.personfilms

import com.al4apps.skillcinema.domain.model.FilmWithPersonModel
import com.al4apps.skillcinema.domain.model.ProfessionKey

data class ProfessionOnFilm(
    val professionKey: ProfessionKey,
    val filmsCount: Int
)

data class FilmsByProfession(
    val professionKey: ProfessionKey,
    val films: List<FilmWithPersonModel>
)