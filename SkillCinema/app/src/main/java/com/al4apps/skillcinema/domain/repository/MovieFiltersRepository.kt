package com.al4apps.skillcinema.domain.repository

import com.al4apps.skillcinema.domain.model.GenresAndCountriesModel

interface MovieFiltersRepository {

    suspend fun getMovieFilters() : GenresAndCountriesModel
}