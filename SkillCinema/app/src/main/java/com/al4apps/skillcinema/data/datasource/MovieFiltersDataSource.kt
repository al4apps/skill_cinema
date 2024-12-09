package com.al4apps.skillcinema.data.datasource

import com.al4apps.skillcinema.data.models.GenresAndCountriesDto

interface MovieFiltersDataSource {
    suspend fun getFilters(): GenresAndCountriesDto
}