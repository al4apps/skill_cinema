package com.al4apps.skillcinema.domain.model

import androidx.annotation.StringRes
import com.al4apps.skillcinema.R

interface Person {
    val personId: Int
    val nameRu: String?
    val nameEn: String?
    val sex: Sex?
    val posterUrl: String
    val profession: String?
    val films: List<FilmWithPerson>
}

interface FilmWithPerson {
    val filmId: Int
    val nameRu: String?
    val nameEn: String?
    val rating: Float?
    val general: Boolean
    val description: String?
    val professionKey: ProfessionKey?
}

enum class Sex {
    MALE,
    FEMALE
}

enum class ProfessionKey(
    @StringRes var title: Int
) {
    ACTOR(R.string.staff_profession_actor_name),
    DIRECTOR(R.string.staff_profession_director_name),
    PRODUCER(R.string.staff_profession_producer_name),
    WRITER(R.string.staff_profession_writer_name),
    HIMSELF(R.string.staff_profession_himself_name)
}