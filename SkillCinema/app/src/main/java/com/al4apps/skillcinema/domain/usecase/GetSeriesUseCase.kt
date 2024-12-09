package com.al4apps.skillcinema.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.al4apps.skillcinema.data.datasource.CollectionMoviesPagingSource
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.MovieModel
import javax.inject.Inject

class GetSeriesUseCase @Inject constructor(
    private val collectionMoviesPagingSource: CollectionMoviesPagingSource.Factory
) {
    fun execute(): Pager<Int, MovieModel> {

        val querySeries = ApiSettings.QueryValues.querySeries

        return Pager(
            config = PagingConfig(pageSize = ApiSettings.PAGE_SIZE),
            pagingSourceFactory = { collectionMoviesPagingSource.create(querySeries) }
        )
    }
}