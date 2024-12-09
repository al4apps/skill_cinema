package com.al4apps.skillcinema.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.al4apps.skillcinema.data.repository.MovieInfoRepositoryImpl
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.ImageModel
import com.al4apps.skillcinema.domain.model.QueryImages
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

private const val FIRST_PAGE = ApiSettings.FIRST_PAGE

class ImagesPagingSource @AssistedInject constructor(
    private val movieInfoRepository: MovieInfoRepositoryImpl,
    @Assisted private val queryParams: QueryImages
) : PagingSource<Int, ImageModel>() {
    override fun getRefreshKey(state: PagingState<Int, ImageModel>): Int {
        return FIRST_PAGE
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageModel> {
        val page = params.key ?: FIRST_PAGE

        kotlin.runCatching {
            movieInfoRepository.getFilmImages(queryParams, page)
        }.fold(
            onSuccess = {
                val prevKey = if (page == FIRST_PAGE) null else page - 1
                val nextKey = if (it.isEmpty()) null else page + 1
                return LoadResult.Page(it, prevKey, nextKey)
            },
            onFailure = {
                return LoadResult.Error(it)
            }
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(queryParams: QueryImages): ImagesPagingSource
    }
}