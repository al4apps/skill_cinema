package com.al4apps.skillcinema.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.al4apps.skillcinema.data.datasource.CollectionMoviesPagingSource
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.MovieModel
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val collectionMoviesPagingSource: CollectionMoviesPagingSource.Factory
) {

    private val popularsQueryParams = ApiSettings.QueryValues.populars

    fun execute(): Pager<Int, MovieModel> {
        return Pager(
            config = PagingConfig(pageSize = ApiSettings.PAGE_SIZE),
            pagingSourceFactory = { collectionMoviesPagingSource.create(popularsQueryParams) }
        )
    }
}