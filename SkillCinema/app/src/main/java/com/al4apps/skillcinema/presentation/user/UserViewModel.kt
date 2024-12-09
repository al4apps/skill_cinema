package com.al4apps.skillcinema.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.model.Collection
import com.al4apps.skillcinema.domain.model.CollectionInfo
import com.al4apps.skillcinema.domain.model.MovieCollectionType
import com.al4apps.skillcinema.domain.model.MoviesAndPersonsCollection
import com.al4apps.skillcinema.domain.usecase.AddCollectionToDbUseCase
import com.al4apps.skillcinema.domain.usecase.CleanCacheUseCase
import com.al4apps.skillcinema.domain.usecase.DeleteCollectionsUseCase
import com.al4apps.skillcinema.domain.usecase.GenerateNextCollectionIdUseCase
import com.al4apps.skillcinema.domain.usecase.GetAllPersonsDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetCollectionsFromDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetMoviesFromDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetMoviesInCollectionDbUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val addCollectionToDbUseCase: AddCollectionToDbUseCase,
    private val generateNextCollectionIdUseCase: GenerateNextCollectionIdUseCase,
    private val deleteCollectionsUseCase: DeleteCollectionsUseCase,
    private val cleanCacheUseCase: CleanCacheUseCase,
    getCollectionsFromDbUseCase: GetCollectionsFromDbUseCase,
    getMoviesFromDbUseCase: GetMoviesFromDbUseCase,
    getMoviesInCollectionDbUseCase: GetMoviesInCollectionDbUseCase,
    getAllPersonsDbUseCase: GetAllPersonsDbUseCase
) : ViewModel() {

    val watchedMoviesLiveData = getMoviesInCollectionDbUseCase
        .executeFlow(Constants.MOVIES_COLLECTION_WATCHED_ID)
        .catch { Timber.d(it) }
        .map { movies ->
            val collectionInfo = CollectionInfo(MovieCollectionType.FROM_DB_WATCHED)
            MoviesAndPersonsCollection(collectionInfo, movies)
        }
        .asLiveData()

    val lastViewsFlow = combine(
        getMoviesFromDbUseCase.execute().catch { Timber.d(it) },
        getAllPersonsDbUseCase.execute().catch { Timber.d(it) }
    ) { movies, persons ->
        val collectionInfo = CollectionInfo(MovieCollectionType.FROM_DB_VIEWS)
        val list = (movies + persons)
            .shuffled()
            .sortedByDescending { it.time }
            .take(Constants.MOVIES_HORIZONTAL_BLOCK_SIZE)
        MoviesAndPersonsCollection(collectionInfo, list)
    }

    val collectionsLiveData = getCollectionsFromDbUseCase.executeFlow()
        .catch { Timber.d(it) }
        .asLiveData()

    @Transaction
    fun addNewCollection(name: String) {
        viewModelScope.launch {
            val id = generateNextCollectionIdUseCase.execute()
            val collection = Collection(id, name, isUsersCollection = true)
            addCollectionToDbUseCase.add(collection)
        }
    }

    fun deleteCollection(collectionId: Int) {
        viewModelScope.launch {
            try {
                deleteCollectionsUseCase.execute(collectionId)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun cleanHistory() {
        viewModelScope.launch {
            try {
                cleanCacheUseCase.clean()
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }
}