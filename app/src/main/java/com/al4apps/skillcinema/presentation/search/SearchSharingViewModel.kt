package com.al4apps.skillcinema.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.al4apps.skillcinema.presentation.searchsettings.CountryUi
import com.al4apps.skillcinema.presentation.searchsettings.GenreOrCountryUi
import com.al4apps.skillcinema.presentation.searchsettings.GenreUi
import com.al4apps.skillcinema.presentation.searchsettings.MovieType
import com.al4apps.skillcinema.presentation.searchsettings.RatingRange
import com.al4apps.skillcinema.presentation.searchsettings.SearchParams
import com.al4apps.skillcinema.presentation.searchsettings.SortType
import com.al4apps.skillcinema.presentation.searchsettings.genreandcountry.SearchGenreCountryFragment
import com.al4apps.skillcinema.presentation.searchsettings.years.Year
import com.al4apps.skillcinema.presentation.searchsettings.years.YearsRange
import com.al4apps.skillcinema.utils.SingleLiveEvent

class SearchSharingViewModel : ViewModel() {

    private var movieType = BasicSearchParams.basicSearchParams.movieType
    private var country: CountryUi? = null
    private var genre: GenreUi? = null
    private var yearsRange: YearsRange = BasicSearchParams.basicSearchParams.yearsRange
    private var ratingRange = BasicSearchParams.basicSearchParams.ratingRange
    private var sortType = BasicSearchParams.basicSearchParams.sortType
    private var hideViewed = BasicSearchParams.basicSearchParams.hideViewed

    private var basicParams = BasicSearchParams.basicSearchParams


    private val _searchParamsLiveData = MutableLiveData(basicParams)
    val searchParamsLiveData: LiveData<SearchParams> get() = _searchParamsLiveData

    private val _selectedGenreCountryLiveData = SingleLiveEvent<GenreOrCountryUi>()
    val selectedGenreOrCountryLiveData: LiveData<GenreOrCountryUi>
        get() = _selectedGenreCountryLiveData

    private val _yearsLiveData = SingleLiveEvent<YearsRange>()
    val yearsLiveData: LiveData<YearsRange> get() = _yearsLiveData

    fun getSearchParams() {
        val params =
            SearchParams(movieType, country, genre, yearsRange, ratingRange, sortType, hideViewed)
        _searchParamsLiveData.value = params
    }

    fun getSavedYearsRange() {
        _yearsLiveData.value = yearsRange
    }

    fun getSelectedGenreOrCountry(type: Int) {
        if (type == SearchGenreCountryFragment.TYPE_COUNTRIES) {
            country?.let { _selectedGenreCountryLiveData.value = it }
        }
        if (type == SearchGenreCountryFragment.TYPE_GENRES) {
            genre?.let { _selectedGenreCountryLiveData.value = it }
        }
    }

    fun saveYearMin(year: Year) {
        yearsRange = YearsRange(
            if (year.isSelected) year
            else null,
            yearsRange.max
        )
    }

    fun saveYearsRange(range: YearsRange) {
        yearsRange = YearsRange(
            range.min,
            range.max
        )
    }

    fun saveMovieType(movieType: MovieType) {
        this.movieType = movieType
    }

    fun saveRating(rating: RatingRange) {
        ratingRange = rating
    }

    fun saveSortType(sortType: SortType) {
        this.sortType = sortType
    }

    fun saveViewedBlock(hideViewed: Boolean) {
        this.hideViewed = hideViewed
    }

    fun saveGenre(item: GenreUi) {
        genre = if (item.isSelected) item
        else null
    }

    fun saveCountry(item: CountryUi) {
        country = if (item.isSelected) item
        else null
    }
}