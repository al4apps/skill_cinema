package com.al4apps.skillcinema.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.skillcinema.domain.model.MoviesCollection
import com.al4apps.skillcinema.domain.usecase.GetMovieCollectionsListUseCase
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getMovieCollectionsListUseCase: GetMovieCollectionsListUseCase
) : ViewModel() {

    private var isStart = true

    private val _movieCollectionsLiveData = MutableLiveData<List<MoviesCollection>>()
    val movieCollectionsLiveData: LiveData<List<MoviesCollection>>
        get() = _movieCollectionsLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> get() = _isLoadingLiveData

    init {
        loadCollections()
    }

    fun loadCollections() {
        viewModelScope.launch {
            try {
                if (isStart) _isLoadingLiveData.value = true
                _movieCollectionsLiveData.value = getMovieCollectionsListUseCase.execute()
            } catch (t: Throwable) {
                Timber.d(t)
            } finally {
                _isLoadingLiveData.value = false
                isStart = false
            }
        }
    }
}