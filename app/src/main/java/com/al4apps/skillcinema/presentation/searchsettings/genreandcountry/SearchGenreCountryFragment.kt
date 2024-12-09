package com.al4apps.skillcinema.presentation.searchsettings.genreandcountry

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentSearchGenreCountryBinding
import com.al4apps.skillcinema.presentation.adapters.search.GenreAndCountryAdapter
import com.al4apps.skillcinema.presentation.main.MainFragment
import com.al4apps.skillcinema.presentation.search.SearchSharingViewModel
import com.al4apps.skillcinema.presentation.searchsettings.CountryUi
import com.al4apps.skillcinema.presentation.searchsettings.GenreOrCountryUi
import com.al4apps.skillcinema.presentation.searchsettings.GenreUi
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.onTextChangedListener
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchGenreCountryFragment :
    AbstractFragment<FragmentSearchGenreCountryBinding>(FragmentSearchGenreCountryBinding::inflate) {

    @Inject
    lateinit var searchFiltersViewModelFactory: SearchFiltersViewModelFactory
    private val viewModel: SearchFiltersViewModel by viewModels { searchFiltersViewModelFactory }
    private val sharingViewModel: SearchSharingViewModel by activityViewModels()
    private var adapter: GenreAndCountryAdapter by autoCleared()
    private val args: SearchGenreCountryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        initSearchEditText()
        if (args.type != MainFragment.NO_ID_DEFAULT && savedInstanceState == null) {
            viewModel.getFilters(args.type)
        }
        sharingViewModel.getSelectedGenreOrCountry(args.type)
        bindViewModel()
    }

    private fun initSearchEditText() {
        if (args.type == TYPE_GENRES) {
            binding.searchEditText.setHint(R.string.search_settings_search_genre_hint)
        }
        if (args.type == TYPE_COUNTRIES) {
            binding.searchEditText.setHint(R.string.search_settings_search_country_hint)
        }
        binding.searchEditText.onTextChangedListener {
            viewModel.filterByText(it)
        }
    }

    private fun initAdapter() {
        adapter = GenreAndCountryAdapter { item ->
            updateSelectedItem(item)
        }
        binding.filterItemsRecyclerView.adapter = adapter
    }

    private fun updateSelectedItem(item: GenreOrCountryUi) {
        viewModel.updateSelectedItem(adapter.items.indexOf(item))

        if (item is CountryUi) {
            sharingViewModel.saveCountry(item)
        }
        if (item is GenreUi) {
            sharingViewModel.saveGenre(item)
        }
    }

    private fun bindViewModel() {
        viewModel.filtersLiveData.observe(viewLifecycleOwner) { list ->
            adapter.items = list
        }
        viewModel.positionToUnselectLiveData.observe(viewLifecycleOwner) { position ->
            adapter.items[position].isSelected = false
            adapter.notifyItemChanged(position)
        }
        sharingViewModel.selectedGenreOrCountryLiveData.observe(viewLifecycleOwner) { item ->
            viewModel.setCurrentSelectedItem(item)
        }
    }

    private fun initToolbar() {
        binding.toolbarIncluded.toolbarWithBack.updateLayoutParamsByWindowInsetsTop()
        binding.toolbarIncluded.toolbarWithBack.isTitleCentered = true
        if (args.type == TYPE_GENRES) {
            binding.toolbarIncluded.toolbarWithBack.setTitle(R.string.search_filters_title_genre)
        }
        if (args.type == TYPE_COUNTRIES) {
            binding.toolbarIncluded.toolbarWithBack.setTitle(R.string.search_filters_title_country)
        }
        binding.toolbarIncluded.toolbarWithBack.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        const val TYPE_GENRES = 1
        const val TYPE_COUNTRIES = 2
    }
}