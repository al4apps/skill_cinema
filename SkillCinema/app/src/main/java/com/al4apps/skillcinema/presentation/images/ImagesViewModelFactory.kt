package com.al4apps.skillcinema.presentation.images

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.data.datasource.ImagesPagingSource
import com.al4apps.skillcinema.domain.usecase.GetFilmPagedImagesUseCase
import javax.inject.Inject

class ImagesViewModelFactory @Inject constructor(
    private val getFilmPagedImagesUseCase: GetFilmPagedImagesUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImagesViewModel::class.java)) {
            return ImagesViewModel(getFilmPagedImagesUseCase) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}