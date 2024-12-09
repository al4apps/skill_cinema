package com.al4apps.skillcinema.presentation.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.al4apps.skillcinema.domain.model.SeasonModel
import com.al4apps.skillcinema.domain.model.SeriesListModel
import com.al4apps.skillcinema.domain.usecase.GetSeriesInfoUseCase
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SeriesViewModel @Inject constructor(
    private val getSeriesInfoUseCase: GetSeriesInfoUseCase
) : ViewModel() {

    private var seasons = emptyList<SeasonModel>()

    private val _seasonsLiveData = MutableLiveData<Int>()
    val seasonsLiveData: LiveData<Int> get() = _seasonsLiveData

    private val _episodesLiveData = MutableLiveData<List<SeriesListModel>>()
    val episodesLiveData: LiveData<List<SeriesListModel>> get() = _episodesLiveData

    fun getSeriesInfo(kinopoiskId: Int) {
        viewModelScope.launch {
            try {
                val series = getSeriesInfoUseCase.execute(kinopoiskId)
                seasons = series.seasons
                _seasonsLiveData.value = series.seasons.size
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun showEpisodesBySeasons(seasonNumber: Int) {
        if (seasons.isEmpty()) return
        val season = seasons.first { it.number == seasonNumber }
        _episodesLiveData.value = listOf(season) + season.episodes
    }
}