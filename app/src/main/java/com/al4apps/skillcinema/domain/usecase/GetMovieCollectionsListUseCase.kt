package com.al4apps.skillcinema.domain.usecase

import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.data.repository.MoviesRepositoryDbImpl
import com.al4apps.skillcinema.data.repository.MoviesRepositoryImpl
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.addFooter
import com.al4apps.skillcinema.domain.toCollectionInfo
import com.al4apps.skillcinema.domain.model.CollectionInfo
import com.al4apps.skillcinema.domain.model.FooterModel
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.model.MovieCollectionType
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.model.MoviesCollection
import javax.inject.Inject

class GetMovieCollectionsListUseCase @Inject constructor(
    private val moviesRepository: MoviesRepositoryImpl,
    private val moviesRepositoryDb: MoviesRepositoryDbImpl,
    private val getPremieresUseCase: GetPremieresUseCase
) {

    private lateinit var watchedIds: List<Int>

    suspend fun execute(): List<MoviesCollection> {
        val footerModel = getFooterModel()
        val filter1 = ApiSettings.getRandomGenreCountryFilter(null)
        val filter2 = ApiSettings.getRandomGenreCountryFilter(filter1)
        watchedIds = moviesRepositoryDb.getMovieIdsInCollectionDb(Constants.MOVIES_COLLECTION_WATCHED_ID)

        val premieres = getPremieresUseCase.execute()
            .addFooter(footerModel)
            .take(COLLECTION_LIST_SIZE)
        val populars = getPopulars().addFooter(footerModel)
        val dynamicList1 = getDynamicCollection(filter1).addFooter(footerModel)
        val top250 = getTop250().addFooter(footerModel)
        val dynamicList2 = getDynamicCollection(filter2).addFooter(footerModel)
        val series = getSeries().addFooter(footerModel)

        val dynamicCollection1 = MoviesCollection(filter1.toCollectionInfo(), dynamicList1)
        val dynamicCollection2 = MoviesCollection(filter2.toCollectionInfo(), dynamicList2)

        return listOf(
            MoviesCollection(CollectionInfo(MovieCollectionType.PREMIER), premieres),
            MoviesCollection(CollectionInfo(MovieCollectionType.POPULAR), populars),
            dynamicCollection1,
            MoviesCollection(CollectionInfo(MovieCollectionType.TOP250), top250),
            dynamicCollection2,
            MoviesCollection(CollectionInfo(MovieCollectionType.SERIES), series)
        )
    }

    private suspend fun getDynamicCollection(filter: GenreCountryFilter): List<MovieModel> {
        return moviesRepository.getDynamicMoviesCollection(filter, ApiSettings.FIRST_PAGE)
            .updateWatchedState()
    }

    private suspend fun getSeries(): List<MovieModel> =
        moviesRepository.getSeries(ApiSettings.QueryValues.querySeries, ApiSettings.FIRST_PAGE)
            .take(COLLECTION_LIST_SIZE)
            .updateWatchedState()

    private suspend fun getTop250(): List<MovieModel> =
        moviesRepository.getMoviesTop250(ApiSettings.QueryValues.top250, ApiSettings.FIRST_PAGE)
            .take(COLLECTION_LIST_SIZE)
            .updateWatchedState()

    private suspend fun getPopulars(): List<MovieModel> =
        moviesRepository.getPopularMovies(ApiSettings.QueryValues.populars, ApiSettings.FIRST_PAGE)
            .take(COLLECTION_LIST_SIZE)
            .updateWatchedState()

    private fun getFooterModel() = FooterModel(R.string.item_movie_show_all_movies_text)

    private fun List<MovieModel>.updateWatchedState(): List<MovieModel> {
        forEach { it.isWatched = watchedIds.contains(it.kinopoiskId) }
        return this
    }

    companion object {
        private const val COLLECTION_LIST_SIZE = 20
    }
}
