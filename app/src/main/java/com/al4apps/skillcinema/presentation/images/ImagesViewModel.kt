package com.al4apps.skillcinema.presentation.images

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.al4apps.skillcinema.domain.model.ImageModel
import com.al4apps.skillcinema.domain.model.QueryImages
import com.al4apps.skillcinema.domain.usecase.GetFilmPagedImagesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ImagesViewModel @Inject constructor(
    private val getFilmPagedImagesUseCase: GetFilmPagedImagesUseCase
) : ViewModel() {

    private val queryParams = MutableStateFlow<QueryImages?>(null)

    val images: Flow<PagingData<ImageModel>> = queryParams.filterNotNull()
        .map { getFilmPagedImagesUseCase.execute(it) }
        .flatMapLatest { it.flow }
        .cachedIn(viewModelScope)

    fun setQuery(queryImages: QueryImages) {
        queryParams.tryEmit(queryImages)
    }
}