package com.al4apps.skillcinema.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.al4apps.skillcinema.data.datasource.CollectionMoviesPagingSource
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.model.QuerySearch
import javax.inject.Inject

class SearchFilmsUseCase @Inject constructor(
    private val collectionMoviesPagingSource: CollectionMoviesPagingSource.Factory
) {
    fun execute(query: QuerySearch): Pager<Int, MovieModel> {
        return Pager(
            PagingConfig(ApiSettings.PAGE_SIZE),
            pagingSourceFactory = { collectionMoviesPagingSource.create(query)}
        )
    }
}