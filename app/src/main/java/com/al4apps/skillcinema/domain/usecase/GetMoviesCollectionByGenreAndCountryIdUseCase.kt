package com.al4apps.skillcinema.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.al4apps.skillcinema.data.datasource.CollectionMoviesPagingSource
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.MovieModel
import javax.inject.Inject

class GetMoviesCollectionByGenreAndCountryIdUseCase @Inject constructor(
    private val collectionMoviesPagingSource: CollectionMoviesPagingSource.Factory
) {
    fun execute(genreCountryFilter: GenreCountryFilter): Pager<Int, MovieModel> {
        return Pager(
            config = PagingConfig(pageSize = ApiSettings.PAGE_SIZE),
            pagingSourceFactory = { collectionMoviesPagingSource.create(genreCountryFilter) }
        )
    }
}