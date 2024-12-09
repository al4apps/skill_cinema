package com.al4apps.skillcinema.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.AddCollectionToDbUseCase
import com.al4apps.skillcinema.domain.usecase.CleanCacheUseCase
import com.al4apps.skillcinema.domain.usecase.DeleteCollectionsUseCase
import com.al4apps.skillcinema.domain.usecase.GenerateNextCollectionIdUseCase
import com.al4apps.skillcinema.domain.usecase.GetAllPersonsDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetCollectionsFromDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetMoviesFromDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetMoviesInCollectionDbUseCase
import javax.inject.Inject

class UserViewModelFactory @Inject constructor(
    private val getCollectionsFromDbUseCase: GetCollectionsFromDbUseCase,
    private val generateNextCollectionIdUseCase: GenerateNextCollectionIdUseCase,
    private val deleteCollectionsUseCase: DeleteCollectionsUseCase,
    private val cleanCacheUseCase: CleanCacheUseCase,
    private val addCollectionToDbUseCase: AddCollectionToDbUseCase,
    private val getMoviesFromDbUseCase: GetMoviesFromDbUseCase,
    private val getMoviesInCollectionDbUseCase: GetMoviesInCollectionDbUseCase,
    private val getAllPersonsDbUseCase: GetAllPersonsDbUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(
                addCollectionToDbUseCase,
                generateNextCollectionIdUseCase,
                deleteCollectionsUseCase,
                cleanCacheUseCase,
                getCollectionsFromDbUseCase,
                getMoviesFromDbUseCase,
                getMoviesInCollectionDbUseCase,
                getAllPersonsDbUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}