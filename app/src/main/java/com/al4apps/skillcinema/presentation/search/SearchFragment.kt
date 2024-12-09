package com.al4apps.skillcinema.presentation.search

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentSearchBinding
import com.al4apps.skillcinema.presentation.adapters.movies.MoviesPagingAdapter
import com.al4apps.skillcinema.presentation.movie.MovieFragment
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.onTextChangedFlow
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : AbstractFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory
    private val viewModel: SearchViewModel by viewModels { searchViewModelFactory }
    private val searchSharingViewModel: SearchSharingViewModel by activityViewModels()
    private var adapter: MoviesPagingAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        initViews()
        getSearchParams()
        initAdapter()
        bindViewModel()
        if (savedInstanceState == null) {
            viewModel.search(binding.searchEditText.onTextChangedFlow())
        }
    }

    private fun getSearchParams() {
        searchSharingViewModel.getSearchParams()
        searchSharingViewModel.searchParamsLiveData.observe(viewLifecycleOwner) { params ->
            viewModel.saveSearchParams(params)
        }
    }

    private fun initViews() {
        binding.root.updateLayoutParamsByWindowInsetsTop()
        binding.searchInputLayout.setEndIconOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchSettingsFragment())
        }
    }

    private fun initAdapter() {
        adapter = MoviesPagingAdapter(true) { kinopoiskId, itemView ->
            navigateToMovie(itemView, kinopoiskId)
        }
        binding.searchListRecyclerView.adapter = adapter

        adapter.addOnPagesUpdatedListener {
            binding.noResultTextView.isVisible =
                adapter.itemCount == 0 && binding.searchEditText.text?.isNotBlank() == true
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.resultsFlow.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun navigateToMovie(itemView: View, kinopoiskId: Int) {
        val transitionName = getString(R.string.movie_transition_name)
        val extras = FragmentNavigatorExtras(itemView to transitionName)
        val action = R.id.navigation_in_movie3
        val argsBundle = bundleOf(MovieFragment.KINOPOISK_ID_KEY to kinopoiskId)
        findNavController().navigate(action, argsBundle, null, extras)
    }
}