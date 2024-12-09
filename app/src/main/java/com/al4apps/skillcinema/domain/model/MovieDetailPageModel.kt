package com.al4apps.skillcinema.domain.model

import androidx.annotation.StringRes
import com.al4apps.skillcinema.R

class MovieDetailPageModel(
    val movieDetail: MovieDetailModel,
    val actors: StaffInfoModel,
    val staff: StaffInfoModel,
    val gallery: GalleryModel,
    val similar: SimilarModel,
    val isSeries: Boolean = movieDetail.serial,
)

enum class StaffType(
    @StringRes val title: Int
) {
    ACTOR(R.string.movie_info_actors_block_title),
    EXCEPT_ACTOR(R.string.movie_info_staff_block_title)
}

data class ImageModel(
    override val imageUrl: String,
    override val previewUrl: String,
) : Image

class GalleryModel(
    val list: List<ImageModel>,
    val count: Int
)

data class StaffModel(
    override val staffId: Int,
    override val nameRu: String,
    override val nameEn: String,
    override val description: String?,
    override val posterUrl: String,
    override val professionText: String,
    override val professionKey: String
) : Staff

class SimilarModel(
    val list: List<SimilarFilmModel>,
    val count: Int
)

data class SimilarFilmModel(
    override val filmId: Int,
    override val nameRu: String,
    override val nameEn: String?,
    override val nameOriginal: String?,
    override val posterUrl: String,
    override val posterUrlPreview: String,
    override val relationType: String
) : SimilarFilm, HomeListModel()