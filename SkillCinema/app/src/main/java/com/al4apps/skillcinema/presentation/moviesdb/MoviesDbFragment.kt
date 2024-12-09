package com.al4apps.skillcinema.presentation.moviesdb

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentMoviesBinding
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.presentation.adapters.movies.DbItemsDelegationAdapter
import com.al4apps.skillcinema.presentation.decor.ItemOffsetDecoration
import com.al4apps.skillcinema.presentation.movie.MovieFragment
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesDbFragment : AbstractFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate) {

    @Inject
    lateinit var moviesDbViewModelFactory: MoviesDbViewModelFactory
    private val viewModel: MoviesDbViewModel by viewModels { moviesDbViewModelFactory }
    private val args: MoviesDbFragmentArgs by navArgs()
    private var adapter: DbItemsDelegationAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        if (savedInstanceState == null) viewModel.getMovies(args.collectionId)
        bindViewModel()
    }

    private fun initAdapter() {
        adapter = DbItemsDelegationAdapter { kinopoiskId, _, view ->
                kinopoiskId?.let { navigateToMovieInfo(kinopoiskId, view) }
            }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(ItemOffsetDecoration(requireContext()))
    }

    private fun initToolbar() {
        binding.toolbarInclude.toolbarWithBack.isTitleCentered = true
        binding.toolbarInclude.toolbarWithBack.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbarInclude.toolbarWithBack.updateLayoutParamsByWindowInsetsTop()
        val title = when (args.collectionId) {
            Constants.MOVIES_COLLECTION_TO_WATCH_ID -> getString(R.string.profile_collection_to_watch_name)
            Constants.MOVIES_COLLECTION_WATCHED_ID -> getString(R.string.movie_type_from_db_watched_title)
            Constants.MOVIES_COLLECTION_LIKES_ID -> getString(R.string.profile_collection_likes_name)
            else -> {
                viewModel.getTitle(args.collectionId)
                ""
            }
        }
        binding.toolbarInclude.toolbarWithBack.title = title

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
        val action = R.id.navigation_in_movie3
        findNavController().navigate(action, argsBundle, null, extra)
    }

    private fun bindViewModel() {
        viewModel.moviesLiveData.observe(viewLifecycleOwner) {
            adapter.items = it
        }
        viewModel.collectionLiveData.observe(viewLifecycleOwner) { collection ->
            binding.toolbarInclude.toolbarWithBack.title = collection.name
        }
    }
}