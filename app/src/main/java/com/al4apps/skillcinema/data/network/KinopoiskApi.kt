package com.al4apps.skillcinema.data.network

import com.al4apps.skillcinema.data.models.GenresAndCountriesDto
import com.al4apps.skillcinema.data.models.movie.FilmDetailDto
import com.al4apps.skillcinema.data.models.movie.SeriesDto
import com.al4apps.skillcinema.data.models.staff.PersonDto
import com.al4apps.skillcinema.data.models.staff.StaffDto
import com.al4apps.skillcinema.data.network.responsemodels.FilmsResponseModel
import com.al4apps.skillcinema.data.network.responsemodels.ImagesResponseModel
import com.al4apps.skillcinema.data.network.responsemodels.PremieresResponseModel
import com.al4apps.skillcinema.data.network.responsemodels.SimilarResponseModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {

    @GET("/api/v2.2/films/premieres")
    suspend fun getPremieres(
        @Query("year") year: Int,
        @Query("month") month: String
    ): PremieresResponseModel

    @GET("/api/v2.2/films/collections")
    suspend fun getMoviesCollection(
        @Query("type") type: String,
        @Query("page") page: Int
    ): FilmsResponseModel

    @GET("/api/v2.2/films")
    suspend fun getFilmsByGenreCountry(
        @Query("countries") country: Int,
        @Query("genres") genre: Int,
        @Query("order") order: String,
        @Query("page") page: Int
    ): FilmsResponseModel

    @GET("/api/v2.2/films")
    suspend fun getPopularSeries(
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("page") page: Int
    ): FilmsResponseModel

    @GET("/api/v2.2/films")
    suspend fun getFilms(
        @Query("countries") country: Int?,
        @Query("genres") genre: Int?,
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int?,
        @Query("yearTo") yearTo: Int?,
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): FilmsResponseModel

    @GET("/api/v2.2/films/{id}")
    suspend fun getMovieDetailInfo(
        @Path("id") kinopoiskId: Int
    ): FilmDetailDto

    @GET("/api/v1/staff")
    suspend fun getMovieStaff(
        @Query("filmId") kinopoiskId: Int
    ): List<StaffDto>

    @GET("/api/v1/staff/{id}")
    suspend fun getPersonInfo(
        @Path("id") id: Int
    ): PersonDto

    @GET("/api/v2.2/films/{id}/images")
    suspend fun getFilmImages(
        @Path("id") kinopoiskId: Int,
        @Query("type") type: String,
        @Query("page") page: Int = 1
    ): ImagesResponseModel

    @GET("/api/v2.2/films/{id}/similars")
    suspend fun getFilmSimilars(
        @Path("id") kinopoiskId: Int
    ): SimilarResponseModel

    @GET("/api/v2.2/films/{id}/seasons")
    suspend fun getSeasonsInfo(
        @Path("id") kinopoiskId: Int
    ): SeriesDto

    @GET("/api/v2.2/films/filters")
    suspend fun getFilmFilters() : GenresAndCountriesDto
}