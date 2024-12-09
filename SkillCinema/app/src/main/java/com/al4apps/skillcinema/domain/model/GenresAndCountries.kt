package com.al4apps.skillcinema.domain.model

interface GenresAndCountries {
    val genres: List<GenreWithId>
    val countries: List<CountryWithId>
}

interface GenreWithId {
    val id: Int
    val genre: String?
}

interface CountryWithId {
    val id: Int
    val country: String?
}

data class GenresAndCountriesModel(
    override val genres: List<GenreWithIdModel>,
    override val countries: List<CountryWithIdModel>
) : GenresAndCountries

data class GenreWithIdModel(
    override val id: Int,
    override val genre: String?
) : GenreWithId

data class CountryWithIdModel(
    override val id: Int,
    override val country: String?
) : CountryWithId