package com.al4apps.skillcinema.presentation.movies

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentMoviesBinding
import com.al4apps.skillcinema.domain.model.DynamicCollectionInfo
import com.al4apps.skillcinema.domain.model.GenreCountryFilter
import com.al4apps.skillcinema.domain.model.MoviesArgsModel
import com.al4apps.skillcinema.presentation.adapters.movies.MoviesPagingAdapter
import com.al4apps.skillcinema.presentation.adapters.movies.MoviesVerticalAdapter
import com.al4apps.skillcinema.presentation.decor.ItemOffsetDecoration
import com.al4apps.skillcinema.presentation.movie.MovieFragment
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.themeColor
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : AbstractFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate) {

    @Inject
    lateinit var moviesViewModelFactory: MoviesViewModelFactory
    private val viewModel: MoviesViewModel by viewModels { moviesViewModelFactory }
    private val args: MoviesFragmentArgs by navArgs()
    private var moviesPagingAdapter: MoviesPagingAdapter by autoCleared()
    private var moviesAdapter: MoviesVerticalAdapter by autoCleared()
    private var hasPagination = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        initToolbar()
        initAdapters()
        if (savedInstanceState == null) getMovies()
        bindViewModel()
        initRefreshLayout()
    }

    private fun initRefreshLayout() {
        binding.moviesSwipeRefresh.apply {
            setOnRefreshListener {
                if (hasPagination) moviesPagingAdapter.refresh()
                else viewModel.refreshList()
            }
            setColorSchemeColors(
                requireActivity().themeColor(com.google.android.material.R.attr.colorPrimary)
            )
        }
    }

    private fun getMovies() {
        val argsModel = MoviesArgsModel(
            args.kinopoiskId, GenreCountryFilter(args.genreId, args.countryId), args.collectionType
        )
        viewModel.loadMovies(argsModel)
    }

    private fun setAdapter(hasPagination: Boolean) {
        binding.recyclerView.adapter = if (hasPagination) moviesPagingAdapter
        else moviesAdapter
        binding.recyclerView.addItemDecoration(ItemOffsetDecoration(requireContext()))
    }

    private fun initAdapters() {
        moviesAdapter = MoviesVerticalAdapter { kinopoiskId, itemView ->
            navigateToMovieInfo(kinopoiskId, itemView)
        }
        moviesPagingAdapter = MoviesPagingAdapter { kinopoiskId, itemView ->
            navigateToMovieInfo(kinopoiskId, itemView)
        }
    }

    private fun navigateToMovieInfo(kinopoiskId: Int, itemView: View) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        val transitionName = getString(R.string.movie_transition_name)
        val extra = FragmentNavigatorExtras(itemView to transitionName)
        val argsBundle = bundleOf(MovieFragment.KINOPOISK_ID_KEY to kinopoiskId)
        val action = if (findNavController().currentDestination?.id == R.id.moviesFragment2) {
            R.id.movieFragment2
        } else {
            R.id.navigation_in_movie3
        }
        findNavController().navigate(action, argsBundle, null, extra)
    }

    private fun bindViewModel() {
        viewModel.isPagingTypeLiveData.observe(viewLifecycleOwner, ::setAdapter)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.moviesLveData.observe(viewLifecycleOwner) { list ->
                binding.moviesSwipeRefresh.isRefreshing = false
                moviesAdapter.submitList(list)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dynamicCollectionInfo.collect { info ->
                info?.let { setDynamicToolbarTitle(info) }
            }
        }

        viewModel.pagedMoviesLiveData.observe(viewLifecycleOwner) {
            moviesPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) {
            binding.moviesSwipeRefresh.isRefreshing = it
        }
    }

    private fun setDynamicToolbarTitle(info: DynamicCollectionInfo) {
        val genre = getString(info.genreRes)
        val country = getString(info.countryRes)
        binding.toolbarInclude.toolbarWithBack.title =
            resources.getString(R.string.movie_type_dynamic_title, genre, country)
    }

    private fun initToolbar() {
        binding.toolbarInclude.toolbarWithBack.isTitleCentered = true
        binding.toolbarInclude.toolbarWithBack.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbarInclude.toolbarWithBack.setTitle(args.collectionType.title)
        binding.toolbarInclude.toolbarWithBack.updateLayoutParamsByWindowInsetsTop()
    }

    companion object {
        const val NO_ID_DEFAULT = -1
    }
}