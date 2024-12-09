package com.al4apps.skillcinema.presentation.personfilms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.domain.model.FilmWithPersonModel
import com.al4apps.skillcinema.domain.model.PersonModel
import com.al4apps.skillcinema.domain.model.ProfessionKey
import com.al4apps.skillcinema.domain.model.Sex
import com.al4apps.skillcinema.domain.usecase.GetPersonInfoUseCase
import com.al4apps.skillcinema.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FilmsWithPersonViewModel @Inject constructor(
    private val getPersonInfoUseCase: GetPersonInfoUseCase
) : ViewModel() {

    private var listFilms = emptyList<FilmsByProfession>()

    private val _personInfoLivedata = MutableLiveData<PersonModel>()
    val personInfoLiveData: LiveData<PersonModel>
        get() = _personInfoLivedata

    private val _filmsLiveData = MutableLiveData<List<FilmWithPersonModel>>()
    val filmsLiveData: LiveData<List<FilmWithPersonModel>>
        get() = _filmsLiveData

    private val _professionOnFilmLiveData = SingleLiveEvent<List<ProfessionOnFilm>>()
    val professionOnFilmLiveData: LiveData<List<ProfessionOnFilm>>
        get() = _professionOnFilmLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> get() = _isLoadingLiveData

    fun getPersonInfo(personId: Int) {
        viewModelScope.launch {
            try {
                _isLoadingLiveData.value = true
                val person = getPersonInfoUseCase.execute(personId)
                _personInfoLivedata.value = person
                filterProfessions(person)
            } catch (t: Throwable) {
                Timber.d(t)
            } finally {
                _isLoadingLiveData.value = false
            }
        }
    }

    fun showFilmsWithProfession(professionKey: ProfessionKey) {
        listFilms.first { it.professionKey == professionKey }.films.also {
            _filmsLiveData.value = it
        }
    }

    private fun filterProfessions(person: PersonModel) {
        val films = person.films.sortedByDescending { it.rating }
        val producer = films.filter { it.professionKey == ProfessionKey.PRODUCER }
        val director = films.filter { it.professionKey == ProfessionKey.DIRECTOR }
        val actor = films.filter { it.professionKey == ProfessionKey.ACTOR }
        val writer = films.filter { it.professionKey == ProfessionKey.WRITER }
        val himself = films.filter { it.professionKey == ProfessionKey.HIMSELF }
            .apply {
                map {
                    it.professionKey?.title =
                        if (person.sex == Sex.MALE) R.string.staff_profession_himself_name
                        else R.string.staff_profession_himself_female_name
                }
            }

        listFilms = listOf(
            FilmsByProfession(ProfessionKey.PRODUCER, producer),
            FilmsByProfession(ProfessionKey.DIRECTOR, director),
            FilmsByProfession(ProfessionKey.ACTOR, actor),
            FilmsByProfession(ProfessionKey.WRITER, writer),
            FilmsByProfession(ProfessionKey.HIMSELF, himself),
        ).sortedByDescending { it.films.size }
            .take(FilmsWithPersonFragment.PROFESSION_CHIPS_COUNT)
            .filter { it.films.isNotEmpty() }

        val professionOnFilms = listFilms.map {
            ProfessionOnFilm(it.professionKey, it.films.size)
        }

        _professionOnFilmLiveData.value = professionOnFilms
    }
}