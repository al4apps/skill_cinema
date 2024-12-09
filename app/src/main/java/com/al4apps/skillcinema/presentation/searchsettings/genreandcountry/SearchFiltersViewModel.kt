package com.al4apps.skillcinema.presentation.searchsettings.genreandcountry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.skillcinema.domain.model.GenresAndCountriesModel
import com.al4apps.skillcinema.domain.usecase.GetGenresAndCountriesUseCase
import com.al4apps.skillcinema.presentation.main.MainFragment
import com.al4apps.skillcinema.presentation.searchsettings.CountryUi
import com.al4apps.skillcinema.presentation.searchsettings.GenreOrCountryUi
import com.al4apps.skillcinema.presentation.searchsettings.GenreUi
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SearchFiltersViewModel @Inject constructor(
    private val getGenresAndCountriesUseCase: GetGenresAndCountriesUseCase
) : ViewModel() {

    private var list: List<GenreOrCountryUi> = emptyList()
    private var selectedItem: GenreOrCountryUi? = null
    private var lastChangedPosition = MainFragment.NO_ID_DEFAULT

    private val _filtersLiveData = MutableLiveData<List<GenreOrCountryUi>>()
    val filtersLiveData: LiveData<List<GenreOrCountryUi>>
        get() = _filtersLiveData

    private val _positionToUnselectLiveData = MutableLiveData<Int>()
    val positionToUnselectLiveData: LiveData<Int> get() = _positionToUnselectLiveData

    fun getFilters(type: Int) {
        viewModelScope.launch {
            try {
                val filters = getGenresAndCountriesUseCase.execute()
                list = filters.filterWithType(type)
                selectedItem?.let { list.updateSelectedPosition(it) }
                _filtersLiveData.value = list
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun setCurrentSelectedItem(item: GenreOrCountryUi) {
        selectedItem = item
    }

    fun filterByText(text: String) {
        _filtersLiveData.value = list.filter { it.name.lowercase().startsWith(text.lowercase()) }
    }

    fun updateSelectedItem(position: Int) {
        if (lastChangedPosition != MainFragment.NO_ID_DEFAULT) {
            _positionToUnselectLiveData.value = lastChangedPosition
        }
        lastChangedPosition = position
    }

    private fun GenresAndCountriesModel.filterWithType(type: Int): List<GenreOrCountryUi> {
        return if (type == SearchGenreCountryFragment.TYPE_GENRES) {
            this.genres.map { GenreUi(it.genre ?: "", it.id) }.filter { it.name.isNotBlank() }
        } else {
            val filtered = this.countries.map { CountryUi(it.country ?: "", it.id) }
                .filter { it.name.isNotBlank() }
            filtered.take(POPULAR_COUNTRIES_COUNT) + filtered.takeLast(filtered.size - POPULAR_COUNTRIES_COUNT)
                .sortedBy { it.name }
        }
    }

    private fun List<GenreOrCountryUi>.updateSelectedPosition(selectedItem: GenreOrCountryUi) {
        find { item -> item.number == selectedItem.number }?.let {
            it.isSelected = true
            lastChangedPosition = list.indexOf(it)
        }
    }

    companion object {
        private const val POPULAR_COUNTRIES_COUNT = 10
    }
}