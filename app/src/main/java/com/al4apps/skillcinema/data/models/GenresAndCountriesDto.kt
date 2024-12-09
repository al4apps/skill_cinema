package com.al4apps.skillcinema.data.models

import com.al4apps.skillcinema.domain.model.CountryWithId
import com.al4apps.skillcinema.domain.model.GenreWithId
import com.al4apps.skillcinema.domain.model.GenresAndCountries
import com.google.gson.annotations.SerializedName

data class GenresAndCountriesDto(
    @SerializedName("genres")
    override val genres: List<GenreWithIdDto>,
    @SerializedName("countries")
    override val countries: List<CountryWithIdDto>
) : GenresAndCountries

data class GenreWithIdDto(
    @SerializedName("id")
    override val id: Int,
    @SerializedName("genre")
    override val genre: String?
) : GenreWithId

data class CountryWithIdDto(
    @SerializedName("id")
    override val id: Int,
    @SerializedName("country")
    override val country: String?
) : CountryWithId