package com.al4apps.skillcinema.data.repository

import com.al4apps.skillcinema.data.datasource.MovieFiltersDataSourceImpl
import com.al4apps.skillcinema.data.models.GenresAndCountriesDto
import com.al4apps.skillcinema.domain.model.CountryWithIdModel
import com.al4apps.skillcinema.domain.model.GenreWithIdModel
import com.al4apps.skillcinema.domain.model.GenresAndCountriesModel
import com.al4apps.skillcinema.domain.repository.MovieFiltersRepository
import javax.inject.Inject

class MovieFiltersRepositoryImpl @Inject constructor(
    private val movieFiltersDataSource: MovieFiltersDataSourceImpl
) : MovieFiltersRepository {
    override suspend fun getMovieFilters(): GenresAndCountriesModel {
        return movieFiltersDataSource.getFilters().toGenresAndCountriesModel()
    }

    private fun GenresAndCountriesDto.toGenresAndCountriesModel(): GenresAndCountriesModel {
        return GenresAndCountriesModel(
            genres = genres.map {
                GenreWithIdModel(it.id, it.genre)
            },
            countries = countries.map {
                CountryWithIdModel(it.id, it.country)
            }
        )
    }
}