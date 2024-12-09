package com.al4apps.skillcinema.presentation.searchsettings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentSearchSettingsBinding
import com.al4apps.skillcinema.presentation.search.BasicSearchParams.RATING_MAX
import com.al4apps.skillcinema.presentation.search.BasicSearchParams.RATING_MIN
import com.al4apps.skillcinema.presentation.search.SearchSharingViewModel
import com.al4apps.skillcinema.presentation.searchsettings.genreandcountry.SearchGenreCountryFragment
import com.al4apps.skillcinema.presentation.searchsettings.years.YearsRange
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop

class SearchSettingsFragment :
    AbstractFragment<FragmentSearchSettingsBinding>(FragmentSearchSettingsBinding::inflate) {

    private val searchSharingViewModel: SearchSharingViewModel by activityViewModels()

    private var checkingNumber = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initParameters()
        initMovieTypeButtonGroup()
        initSortTypeButtonGroup()
        initRatingRangeSlider()
        searchSharingViewModel.getSearchParams()
        bindViewModel()
    }

    private fun initToolbar() {
        binding.toolbarIncluded.toolbarWithBack.updateLayoutParamsByWindowInsetsTop()
        binding.toolbarIncluded.toolbarWithBack.isTitleCentered = true
        binding.toolbarIncluded.toolbarWithBack.setTitle(R.string.search_settings_toolbar_title)
        binding.toolbarIncluded.toolbarWithBack.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindViewModel() {
        searchSharingViewModel.searchParamsLiveData.observe(viewLifecycleOwner) { searchParams ->
            setSearchParams(searchParams)
        }
    }

    private fun initParameters() {
        binding.countryBlock.parameterNameTextView.setText(R.string.search_settings_country)
        binding.countryBlock.container.setOnClickListener {
            navigateToFilters(SearchGenreCountryFragment.TYPE_COUNTRIES)
        }
        binding.genreBlock.parameterNameTextView.setText(R.string.search_settings_genre)
        binding.genreBlock.container.setOnClickListener {
            navigateToFilters(SearchGenreCountryFragment.TYPE_GENRES)
        }
        binding.yearParameterBlock.parameterNameTextView.setText(R.string.search_settings_year)
        binding.yearParameterBlock.container.setOnClickListener {
            findNavController().navigate(
                SearchSettingsFragmentDirections.actionSearchSettingsFragmentToYearsFragment()
            )
        }
        binding.ratingParameterBlock.parameterNameTextView.setText(R.string.search_settings_rating)
        binding.viewedParameterBlock.container.setOnClickListener {
            setViewedBlock(!binding.viewedParameterBlock.hideViewedInvisibleSwitcher.isChecked)
        }
    }

    private fun setSearchParams(searchParams: SearchParams) {
        setMovieType(searchParams.movieType)
        binding.countryBlock.parameterSelectedTextView.text =
            searchParams.country?.countryName ?: getString(R.string.movie_country_any)
        binding.genreBlock.parameterSelectedTextView.text =
            searchParams.genre?.genreName ?: getString(R.string.movie_genre_any)
        setYearsText(searchParams.yearsRange)
        setRatingText(searchParams.ratingRange)
        setSortType(searchParams.sortType)
        binding.ratingSlider1.setValues(
            searchParams.ratingRange.min,
            searchParams.ratingRange.max
        )
        setViewedBlock(searchParams.hideViewed)
    }

    private fun initRatingRangeSlider() {
        binding.ratingSlider1.setLabelFormatter { it.toInt().toString() }
        binding.ratingSlider1.setMinSeparationValue(1f)
        binding.ratingSlider1.setCustomThumbDrawable(R.drawable.ellipse)
        binding.ratingSlider1.addOnChangeListener { slider, value, _ ->
            val valueInt = value.toInt()
            if (valueInt != checkingNumber) {
                checkingNumber = valueInt
                val min = slider.values.min()
                val max = slider.values.max()
                val range = RatingRange(min, max)
                searchSharingViewModel.saveRating(range)
                setRatingText(range)
            }
        }
    }

    private fun setRatingText(range: RatingRange) {
        val text = when {
            range.min.toInt() == RATING_MIN.toInt() && range.max.toInt() == RATING_MAX.toInt() -> {
                getString(R.string.search_settings_rating_text_any)
            }

            range.min.toInt() == RATING_MIN.toInt() -> {
                getString(R.string.search_settings_rating_text_from_0_to, range.max.toInt())
            }

            range.max.toInt() == RATING_MAX.toInt() -> {
                getString(R.string.search_settings_rating_text_from_number, range.min.toInt())
            }

            else -> getString(
                R.string.search_settings_rating_text_from_n_to_m,
                range.min.toInt(),
                range.max.toInt()
            )
        }
        binding.ratingParameterBlock.parameterSelectedTextView.text = text
    }

    private fun setYearsText(yearsRange: YearsRange) {
        binding.yearParameterBlock.parameterSelectedTextView.text = when {
            yearsRange.min == yearsRange.max && yearsRange.max!= null -> {
                getString(R.string.search_years_range_1_year, yearsRange.max.year)
            }

            yearsRange.min != null && yearsRange.max != null -> {
                getString(
                    R.string.search_settings_year_min_max,
                    yearsRange.min.year,
                    yearsRange.max.year
                )
            }

            yearsRange.max != null -> {
                getString(R.string.search_settings_year_max, yearsRange.max.year)
            }

            yearsRange.min != null -> {
                getString(R.string.search_settings_year_min, yearsRange.min.year)
            }

            else -> {
                getString(R.string.search_settings_year_text_any)
            }
        }
    }

    private fun setViewedBlock(hideViewed: Boolean) {
        binding.viewedParameterBlock.hideViewedInvisibleSwitcher.isChecked = hideViewed
        searchSharingViewModel.saveViewedBlock(hideViewed)
        if (hideViewed) {
            binding.viewedParameterBlock.eyeImageView.setImageResource(R.drawable.eye_closed)
            binding.viewedParameterBlock.parameterNameTextView.setText(R.string.search_settings_not_viewed_text)
        } else {
            binding.viewedParameterBlock.eyeImageView.setImageResource(R.drawable.eye_blue)
            binding.viewedParameterBlock.parameterNameTextView.setText(R.string.search_settings_viewed_text)
        }
    }

    private fun setMovieType(movieType: MovieType) {
        val id = when (movieType) {
            MovieType.ALL -> binding.allButton.id
            MovieType.FILM -> binding.filmsButton.id
            MovieType.SERIES -> binding.seriesButton.id
        }
        binding.movieTypeToggleButton.check(id)
    }

    private fun setSortType(sortType: SortType) {
        val id = when (sortType) {
            SortType.DATE -> binding.dateButton1.id
            SortType.POPULARITY -> binding.popularsButton1.id
            SortType.RATING -> binding.ratingButton1.id
        }
        binding.sortTypeToggleButton1.check(id)
    }

    private fun initMovieTypeButtonGroup() {
        binding.movieTypeToggleButton.addOnButtonCheckedListener { _, checkedId, _ ->
            if (binding.movieTypeToggleButton.checkedButtonId == checkedId) {
                val movieType = when (checkedId) {
                    binding.allButton.id -> MovieType.ALL
                    binding.filmsButton.id -> MovieType.FILM
                    binding.seriesButton.id -> MovieType.SERIES
                    else -> MovieType.ALL
                }
                searchSharingViewModel.saveMovieType(movieType)
            }
        }
    }

    private fun initSortTypeButtonGroup() {
        binding.sortTypeToggleButton1.addOnButtonCheckedListener { _, checkedId, _ ->
            if (binding.sortTypeToggleButton1.checkedButtonId == checkedId) {
                val sortType = when (checkedId) {
                    binding.dateButton1.id -> SortType.DATE
                    binding.popularsButton1.id -> SortType.POPULARITY
                    binding.ratingButton1.id -> SortType.RATING
                    else -> SortType.DATE
                }
                searchSharingViewModel.saveSortType(sortType)
            }
        }
    }

    private fun navigateToFilters(type: Int) {
        val action = SearchSettingsFragmentDirections
            .actionSearchSettingsFragmentToSearchGenreCountryFragment(type)
        findNavController().navigate(action)
    }
}