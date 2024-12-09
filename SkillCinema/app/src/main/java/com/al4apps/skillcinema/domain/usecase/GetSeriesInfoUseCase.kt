package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MovieInfoRepositoryImpl
import com.al4apps.skillcinema.domain.model.SeriesInfoModel
import com.al4apps.skillcinema.domain.model.SeriesModel
import javax.inject.Inject

class GetSeriesInfoUseCase @Inject constructor(
    private val movieInfoRepository: MovieInfoRepositoryImpl
) {
    suspend fun execute(kinopoiskId: Int): SeriesModel {
        return movieInfoRepository.getSeriesInfo(kinopoiskId)
    }
}