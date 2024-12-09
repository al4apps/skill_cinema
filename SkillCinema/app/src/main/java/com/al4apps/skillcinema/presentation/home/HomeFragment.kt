package com.al4apps.skillcinema.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentHomeBinding
import com.al4apps.skillcinema.domain.model.CollectionInfo
import com.al4apps.skillcinema.domain.model.MovieCollectionType
import com.al4apps.skillcinema.domain.model.MoviesCollection
import com.al4apps.skillcinema.presentation.adapters.movies.MoviesDelegationAdapter
import com.al4apps.skillcinema.presentation.main.MainFragment
import com.al4apps.skillcinema.presentation.main.SharingViewModel
import com.al4apps.skillcinema.presentation.movie.MovieFragment
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.themeColor
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : AbstractFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory
    private val viewModel: HomeViewModel by viewModels { homeViewModelFactory }

    private var premieresAdapter: MoviesDelegationAdapter by autoCleared()
    private var popularsAdapter: MoviesDelegationAdapter by autoCleared()
    private var dynamic1Adapter: MoviesDelegationAdapter by autoCleared()
    private var top250Adapter: MoviesDelegationAdapter by autoCleared()
    private var dynamic2Adapter: MoviesDelegationAdapter by autoCleared()
    private var seriesAdapter: MoviesDelegationAdapter by autoCleared()
    private val sharingViewModel: SharingViewModel by activityViewModels()

    private var dynamicInfo1: CollectionInfo? = null
    private var dynamicInfo2: CollectionInfo? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        binding.homeSwipeRefresh.updateLayoutParamsByWindowInsetsTop()
        initAdapters()
        bindViewModel()
        initRefreshLayout()
        observeLoadingState(savedInstanceState == null)
    }

    private fun initRefreshLayout() {
        binding.homeSwipeRefresh.setOnRefreshListener { viewModel.loadCollections() }
        binding.homeSwipeRefresh.setColorSchemeColors(
            requireActivity().themeColor(com.google.android.material.R.attr.colorPrimary)
        )
    }

    private fun updateCollectionsList(movieCollections: List<MoviesCollection>) {
        val dynamics = movieCollections.filter { it.info.type == MovieCollectionType.DYNAMIC }
        if (dynamics.isNotEmpty()) dynamicInfo1 = dynamics[0].info
        if (dynamics.size > 1) dynamicInfo2 = dynamics[1].info
        initDynamicAdapters()
        initBlocks(movieCollections)
        initBlocksListeners()
    }

    private fun initAdapters() {
        premieresAdapter = getAdapter(CollectionInfo(MovieCollectionType.PREMIER))
        popularsAdapter = getAdapter(CollectionInfo(MovieCollectionType.POPULAR))
        top250Adapter = getAdapter(CollectionInfo(MovieCollectionType.TOP250))
        seriesAdapter = getAdapter(CollectionInfo(MovieCollectionType.SERIES))

        binding.premieresBlock.setMoviesAdapter(premieresAdapter)
        binding.popularsBlock.setMoviesAdapter(popularsAdapter)
        binding.top250Block.setMoviesAdapter(top250Adapter)
        binding.seriesBlock.setMoviesAdapter(seriesAdapter)
    }

    private fun initDynamicAdapters() {
        dynamicInfo1?.let {
            dynamic1Adapter = getAdapter(it)
            binding.dynamic1Block.cleanInfo()
            binding.dynamic1Block.setMoviesAdapter(dynamic1Adapter)
        }
        dynamicInfo2?.let {
            dynamic2Adapter = getAdapter(it)
            binding.dynamic2Block.cleanInfo()
            binding.dynamic2Block.setMoviesAdapter(dynamic2Adapter)
        }
    }

    private fun initBlocks(movieCollections: List<MoviesCollection>) {
        val premieres = movieCollections.firstByType(MovieCollectionType.PREMIER)
        val populars = movieCollections.firstByType(MovieCollectionType.POPULAR)
        val dynamic1 = movieCollections.first { it.info == dynamicInfo1 }
        val top250 = movieCollections.firstByType(MovieCollectionType.TOP250)
        val dynamic2 = movieCollections.first { it.info == dynamicInfo2 }
        val series = movieCollections.firstByType(MovieCollectionType.SERIES)


        binding.premieresBlock.setMoviesCollection(premieres)
        binding.popularsBlock.setMoviesCollection(populars)
        binding.dynamic1Block.setMoviesCollection(dynamic1)
        binding.top250Block.setMoviesCollection(top250)
        binding.dynamic2Block.setMoviesCollection(dynamic2)
        binding.seriesBlock.setMoviesCollection(series)
    }

    private fun initBlocksListeners() {
        binding.premieresBlock.allTextView.setOnClickListener {
            navigateToCollectionList(CollectionInfo(MovieCollectionType.PREMIER))
        }
        binding.popularsBlock.allTextView.setOnClickListener {
            navigateToCollectionList(CollectionInfo(MovieCollectionType.POPULAR))
        }
        dynamicInfo1?.let { info ->
            binding.dynamic1Block.allTextView.setOnClickListener {
                navigateToCollectionList(info)
            }
        }
        binding.top250Block.allTextView.setOnClickListener {
            navigateToCollectionList(CollectionInfo(MovieCollectionType.TOP250))
        }
        dynamicInfo2?.let { info ->
            binding.dynamic2Block.allTextView.setOnClickListener {
                navigateToCollectionList(info)
            }
        }
        binding.seriesBlock.allTextView.setOnClickListener {
            navigateToCollectionList(CollectionInfo(MovieCollectionType.SERIES))
        }

    }

    private fun getAdapter(collectionInfo: CollectionInfo): MoviesDelegationAdapter {
        return MoviesDelegationAdapter(onItemClick = { kinopoiskId: Int, itemView: View ->
            navigateToMovieInfo(kinopoiskId, itemView)
        }, onShowAllClick = {
            navigateToCollectionList(collectionInfo)
        })
    }

    private fun navigateToMovieInfo(kinopoiskId: Int, itemView: View) {
        val transitionName = getString(R.string.movie_transition_name)
        val extras = FragmentNavigatorExtras(itemView to transitionName)
        val action = R.id.navigation_in_movie3
        val argsBundle = bundleOf(MovieFragment.KINOPOISK_ID_KEY to kinopoiskId)
        findNavController().navigate(action, argsBundle, null, extras)
    }

    private fun navigateToCollectionList(collectionInfo: CollectionInfo) {
        val action = HomeFragmentDirections.actionHomeFragmentToMoviesFragment(
            collectionInfo.type,
            collectionInfo.dynamicCollectionInfo?.genreId ?: MainFragment.NO_ID_DEFAULT,
            collectionInfo.dynamicCollectionInfo?.countryId ?: MainFragment.NO_ID_DEFAULT
        )
        findNavController().navigate(action)
    }

    private fun observeLoadingState(isStart: Boolean) {
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            if (isStart) sharingViewModel.updateLoadingState(isLoading)
            binding.homeSwipeRefresh.isRefreshing = isLoading
        }
    }

    private fun bindViewModel() {
        viewModel.movieCollectionsLiveData.observe(viewLifecycleOwner) { movieCollections ->
            updateCollectionsList(movieCollections)
            binding.homeSwipeRefresh.isRefreshing = false
        }
    }

    private fun List<MoviesCollection>.firstByType(type: MovieCollectionType): MoviesCollection {
        return first { it.info.type == type }
    }
}