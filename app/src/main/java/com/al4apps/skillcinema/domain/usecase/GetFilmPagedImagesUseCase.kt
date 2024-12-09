package com.al4apps.skillcinema.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.al4apps.skillcinema.data.datasource.ImagesPagingSource
import com.al4apps.skillcinema.data.settings.Settings
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.model.ImageModel
import com.al4apps.skillcinema.domain.model.QueryImages
import javax.inject.Inject

class GetFilmPagedImagesUseCase @Inject constructor(
    private val imagesPagingSource: ImagesPagingSource.Factory
) {
    fun execute(queryImages: QueryImages): Pager<Int, ImageModel> {
        return Pager(config = PagingConfig(pageSize = ApiSettings.PAGE_SIZE),
            pagingSourceFactory = { imagesPagingSource.create(queryImages) }
        )
    }
}