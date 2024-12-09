package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.data.repository.MovieInfoRepositoryImpl
import com.al4apps.skillcinema.data.repository.StaffRepositoryImpl
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.GalleryModel
import com.al4apps.skillcinema.domain.model.ImageType
import com.al4apps.skillcinema.domain.model.QueryImages
import com.al4apps.skillcinema.domain.model.MovieDetailPageModel
import com.al4apps.skillcinema.domain.model.SimilarModel
import com.al4apps.skillcinema.domain.model.StaffInfoModel
import com.al4apps.skillcinema.domain.model.StaffType
import javax.inject.Inject

class GetMovieInfoUseCase @Inject constructor(
    private val movieInfoRepository: MovieInfoRepositoryImpl,
    private val staffRepository: StaffRepositoryImpl
) {
    suspend fun execute(kinopoiskId: Int): MovieDetailPageModel {
        val imageParams = QueryImages(kinopoiskId, ImageType.STILL)
        val movieDetail = movieInfoRepository.getMovieInfo(kinopoiskId)
        val allStaff = staffRepository.getStaffByKinopoiskId(kinopoiskId)
        val actors = allStaff.filter { it.professionKey == ApiSettings.Staff.PROFESSION_KEY_ACTOR }
        val staff = allStaff.filter {
            it.professionKey == ApiSettings.Staff.PROFESSION_KEY_DIRECTOR
                    || it.professionKey == ApiSettings.Staff.PROFESSION_KEY_PRODUCER
                    || it.professionKey == ApiSettings.Staff.PROFESSION_KEY_WRITER
        }
        val images = movieInfoRepository.getFilmImages(imageParams, ApiSettings.FIRST_PAGE)
        val gallery = GalleryModel(images.take(GALLERY_IMAGES_COUNT), images.size)
        val similarFilms = movieInfoRepository.getSimilarFilmsById(kinopoiskId)
        val similar = SimilarModel(similarFilms.take(SIMILAR_COUNT), similarFilms.size)

        return MovieDetailPageModel(
            movieDetail = movieDetail,
            actors = StaffInfoModel(actors.take(ACTORS_COUNT), StaffType.ACTOR, actors.size),
            staff = StaffInfoModel(staff.take(STAFF_COUNT), StaffType.EXCEPT_ACTOR, staff.size),
            gallery = gallery,
            similar = similar
        )
    }

    companion object {
        private const val GALLERY_IMAGES_COUNT = 20
        private const val SIMILAR_COUNT = 20
        private const val ACTORS_COUNT = 20
        private const val STAFF_COUNT = 6
    }
}