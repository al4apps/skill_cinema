package com.al4apps.skillcinema.presentation.moviesdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.skillcinema.domain.model.Collection
import com.al4apps.skillcinema.domain.model.MovieDbModel
import com.al4apps.skillcinema.domain.usecase.GetCollectionsFromDbUseCase
import com.al4apps.skillcinema.domain.usecase.GetMoviesInCollectionDbUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MoviesDbViewModel @Inject constructor(
    private val getMoviesInCollectionDbUseCase: GetMoviesInCollectionDbUseCase,
    private val getCollectionsFromDbUseCase: GetCollectionsFromDbUseCase
) : ViewModel() {

    private var isInitialized = false

    private val _moviesLiveData = MutableLiveData<List<MovieDbModel>>()
    val moviesLiveData: LiveData<List<MovieDbModel>>
        get() = _moviesLiveData

    private val _collectionLiveData = MutableLiveData<Collection>()
    val collectionLiveData: LiveData<Collection>
        get() = _collectionLiveData

    fun getMovies(collectionId: Int) {
        if (isInitialized) return
        getMoviesInCollectionDbUseCase.executeFlow(collectionId)
            .catch { Timber.d(it) }
            .mapLatest {
                _moviesLiveData.value = it
            }
            .launchIn(viewModelScope)
        isInitialized = true
    }

    fun getTitle(collectionId: Int) {
        viewModelScope.launch {
            try {
                _collectionLiveData.value = getCollectionsFromDbUseCase.execute(collectionId)
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }
}