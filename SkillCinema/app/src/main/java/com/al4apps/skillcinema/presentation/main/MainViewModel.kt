package com.al4apps.skillcinema.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.model.Collection
import com.al4apps.skillcinema.domain.usecase.AddCollectionToDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetCollectionsFromDbUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val addCollectionToDbUseCase: AddCollectionToDbUseCase,
    private val getCollectionsFromDbUseCase: GetCollectionsFromDbUseCase
) : ViewModel() {

    private val likesCollection = Collection(
        id = Constants.MOVIES_COLLECTION_LIKES_ID,
        isUsersCollection = false
    )
    private val toWatchCollection = Collection(
        id = Constants.MOVIES_COLLECTION_TO_WATCH_ID,
        isUsersCollection = false
    )
    private val watchedCollection = Collection(
        id = Constants.MOVIES_COLLECTION_WATCHED_ID,
        isUsersCollection = false
    )

    init {
        viewModelScope.launch {
            val standardCollections = getCollectionsFromDbUseCase.execute(true)
            if (!standardCollections.map { it.id }.contains(likesCollection.id)) {
                addCollectionToDbUseCase.add(likesCollection)
            }
            if (!standardCollections.map { it.id }.contains(toWatchCollection.id)) {
                addCollectionToDbUseCase.add(toWatchCollection)
            }
            if (!standardCollections.map { it.id }.contains(watchedCollection.id)) {
                addCollectionToDbUseCase.add(watchedCollection)
            }
        }
    }
}