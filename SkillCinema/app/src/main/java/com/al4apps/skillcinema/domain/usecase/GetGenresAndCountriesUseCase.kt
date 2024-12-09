package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MovieFiltersRepositoryImpl
import com.al4apps.skillcinema.domain.model.GenresAndCountriesModel
import javax.inject.Inject

class GetGenresAndCountriesUseCase @Inject constructor(
    private val movieFiltersRepository: MovieFiltersRepositoryImpl
) {
    suspend fun execute(): GenresAndCountriesModel {
        return movieFiltersRepository.getMovieFilters()
    }
}