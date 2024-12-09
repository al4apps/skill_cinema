package com.al4apps.skillcinema.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.al4apps.skillcinema.data.repository.MoviesRepositoryImpl
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.model.QueryParams
import com.al4apps.skillcinema.domain.model.QueryPopular
import com.al4apps.skillcinema.domain.model.QuerySearch
import com.al4apps.skillcinema.domain.model.QuerySeries
import com.al4apps.skillcinema.domain.model.Top250
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val FIRST_PAGE = ApiSettings.FIRST_PAGE

class CollectionMoviesPagingSource @AssistedInject constructor(
    private val moviesRepository: MoviesRepositoryImpl,
    @Assisted private val queryParams: QueryParams
) : PagingSource<Int, MovieModel>() {
    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int {
        return FIRST_PAGE
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        val page = params.key ?: ApiSettings.FIRST_PAGE

        kotlin.runCatching {
            when (queryParams) {
                is Top250 -> {
                    moviesRepository.getMoviesTop250(queryParams, page)
                }

                is QueryPopular -> {
                    moviesRepository.getPopularMovies(queryParams, page)
                }

                is QuerySeries -> {
                    moviesRepository.getSeries(queryParams, page)
                }

                is GenreCountryFilter -> {
                    moviesRepository.getDynamicMoviesCollection(queryParams, page)
                }

                is QuerySearch -> {
                    moviesRepository.getFilmsByParams(queryParams, page)
                }

                else -> {
                    emptyList()
                }
            }
        }.fold(
            onSuccess = { list ->
                val prevKey = if (page == FIRST_PAGE) null else page - 1
                val nextKey = if (list.isEmpty()) null else page + 1
                return LoadResult.Page(list, prevKey, nextKey)
            },
            onFailure = {
                return LoadResult.Error(it)
            }
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(queryParams: QueryParams): CollectionMoviesPagingSource
    }
}