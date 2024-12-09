package com.al4apps.skillcinema.presentation.staff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.domain.model.CollectionInfo
import com.al4apps.skillcinema.domain.model.MovieCollectionType
import com.al4apps.skillcinema.domain.model.MoviesCollection
import com.al4apps.skillcinema.domain.model.PersonModel
import com.al4apps.skillcinema.domain.usecase.GetFilmsListByIdsUseCase
import com.al4apps.skillcinema.domain.usecase.GetPersonInfoUseCase
import com.al4apps.skillcinema.domain.usecase.SavePersonInDbUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class PersonViewModel @Inject constructor(
    private val getPersonInfoUseCase: GetPersonInfoUseCase,
    private val getFilmsListByIdsUseCase: GetFilmsListByIdsUseCase,
    private val savePersonInDbUseCase: SavePersonInDbUseCase
) : ViewModel() {

    private var isInfoLoaded = false

    private val _personLiveData = MutableLiveData<PersonModel>()
    val personLiveData: LiveData<PersonModel>
        get() = _personLiveData

    private val _bestFilmsLiveData = MutableLiveData<MoviesCollection>()
    val bestFilmsLiveData: LiveData<MoviesCollection> get() = _bestFilmsLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean>
        get() = _isLoadingLiveData

    fun getPersonInfo(personId: Int) {
        if (isInfoLoaded) return
        viewModelScope.launch {
            try {
                _isLoadingLiveData.value = true

                val person = getPersonInfoUseCase.execute(personId)
                _personLiveData.value = person
                cachePersonInDb(person)
                val bestList = person.films
                    .sortedByDescending { it.rating }
                    .map { it.filmId }
                    .toSet()
                    .take(BEST_FILMS_COUNT)
                getBestFilmsList(bestList)
                isInfoLoaded = true
            } catch (t: Throwable) {
                Timber.d(t)
            } finally {
                _isLoadingLiveData.value = false
            }
        }
    }

    private suspend fun getBestFilmsList(list: List<Int>) {
        try {
            val films = getFilmsListByIdsUseCase.execute(list)
            val collection = MoviesCollection(
                CollectionInfo(MovieCollectionType.WITH_PERSON, R.string.staff_best_films_title),
                films
            )
            _bestFilmsLiveData.value = collection
        } catch (t: Throwable) {
            Timber.d(t)
        }
    }

    private suspend fun cachePersonInDb(person: PersonModel) {
        savePersonInDbUseCase.save(person)
    }

    companion object {
        private const val BEST_FILMS_COUNT = 8
    }
}