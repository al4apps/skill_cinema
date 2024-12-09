package com.al4apps.skillcinema.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.al4apps.skillcinema.data.datasource.CollectionMoviesPagingSource
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.MovieModel
import javax.inject.Inject

class GetTop250UseCase @Inject constructor(
    private val collectionMoviesPagingSource: CollectionMoviesPagingSource.Factory
) {
    fun execute(): Pager<Int, MovieModel> {

        val top250QueryParams = ApiSettings.QueryValues.top250

        return Pager(
            config = PagingConfig(pageSize = ApiSettings.PAGE_SIZE),
            pagingSourceFactory = { collectionMoviesPagingSource.create(top250QueryParams) }
        )
    }
}