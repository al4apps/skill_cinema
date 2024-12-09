package com.al4apps.skillcinema.data.datasource

import com.al4apps.skillcinema.data.models.GenresAndCountriesDto
import com.al4apps.skillcinema.data.network.KinopoiskApi
import javax.inject.Inject

class MovieFiltersDataSourceImpl @Inject constructor(
    private val kinopoiskApi: KinopoiskApi
) : MovieFiltersDataSource {
    override suspend fun getFilters(): GenresAndCountriesDto {
        return kinopoiskApi.getFilmFilters()
    }
}