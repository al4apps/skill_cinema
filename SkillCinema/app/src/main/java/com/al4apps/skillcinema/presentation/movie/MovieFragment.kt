package com.al4apps.skillcinema.presentation.movie

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.DialogLayoutBinding
import com.al4apps.skillcinema.databinding.FragmentMovieBinding
import com.al4apps.skillcinema.domain.ApiSettings
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.model.MovieCollectionType
import com.al4apps.skillcinema.domain.model.MovieDetailModel
import com.al4apps.skillcinema.domain.model.MovieDetailPageModel
import com.al4apps.skillcinema.domain.model.SeriesInfoModel
import com.al4apps.skillcinema.domain.model.StaffType
import com.al4apps.skillcinema.presentation.adapters.StaffAdapter
import com.al4apps.skillcinema.presentation.adapters.collections.CollectionsListDelegationAdapter
import com.al4apps.skillcinema.presentation.adapters.images.ImagesAdapter
import com.al4apps.skillcinema.presentation.adapters.movies.MoviesDelegationAdapter
import com.al4apps.skillcinema.presentation.main.MainFragment.Companion.NO_ID_DEFAULT
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.onTextChangedListener
import com.al4apps.skillcinema.utils.themeColor
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : AbstractFragment<FragmentMovieBinding>(
    FragmentMovieBinding::inflate
) {

    @Inject
    lateinit var movieViewModelFactory: MovieViewModelFactory
    private val viewModel: MovieViewModel by viewModels { movieViewModelFactory }
    private val args: MovieFragmentArgs by navArgs()
    private var staffAdapter: StaffAdapter by autoCleared()
    private var actorsAdapter: StaffAdapter by autoCleared()
    private var imagesAdapter: ImagesAdapter by autoCleared()
    private var similarFilmsAdapter: MoviesDelegationAdapter by autoCleared()
    private var collectionsAdapter: CollectionsListDelegationAdapter by autoCleared()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayoutCompat>

    private var kinopoiskId: Int = NO_ID_DEFAULT
    private var isDescriptionCropped = false
    private var fullDescription: String? = null
    private var filmName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val superOnCreateView = super.onCreateView(inflater, container, savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.mainNavHostContainer
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
        }
        bottomSheetBehavior = BottomSheetBehavior.from(binding.collectionsBottomSheet)
        return superOnCreateView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        setMovieId()
        if (savedInstanceState == null && kinopoiskId != NO_ID_DEFAULT) {
            viewModel.getMovieInfo(kinopoiskId)
        }
        initAdapters()
        initBlocks()
        initViews()
        initActionsWithMovie()
        bindViewModel()
    }

    private fun initViews() {
        binding.toolbarWithBackIcon.setNavigationOnClickListener { findNavController().popBackStack() }

        binding.descriptionTextView.setOnClickListener {
            fullDescription?.let { description ->
                if (isDescriptionCropped) {
                    binding.descriptionTextView.text = fullDescription
                    isDescriptionCropped = !isDescriptionCropped
                } else setDescription(description)
            }
        }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setMovieId() {
        kinopoiskId = args.kinopoiskId
        if (kinopoiskId == NO_ID_DEFAULT) {
            kinopoiskId = arguments?.getInt(KINOPOISK_ID_KEY) ?: 0
        }
        if (kinopoiskId == 0) kinopoiskId = NO_ID_DEFAULT
    }

    private fun initAdapters() {
        staffAdapter = StaffAdapter(isMatchParent = false) { id, itemView ->
            navigateToPersonInfo(id, itemView)
        }
        actorsAdapter = StaffAdapter(isMatchParent = false) { id, itemView ->
            navigateToPersonInfo(id, itemView)
        }
        imagesAdapter = ImagesAdapter { url, itemView ->
            showImage(url, itemView)
        }
        similarFilmsAdapter = MoviesDelegationAdapter({ kinopoiskId, itemView ->
            navigateToSelf(kinopoiskId, itemView)
        }, {})

        collectionsAdapter =
            CollectionsListDelegationAdapter(onAddCollectionClick = { showDialogNewCollection() },
                onCheckboxClick = { collectionId ->
                    viewModel.addMovieToCollection(collectionId)
                })
        binding.bottomSheetRecyclerView.adapter = collectionsAdapter
    }

    private fun initBlocks() {
        binding.staffBlock.setAdapter(staffAdapter)
        binding.actorsBlock.setAdapter(actorsAdapter)
        binding.imagesBlock.setAdapter(imagesAdapter)
        binding.similarBlock.setMoviesAdapter(similarFilmsAdapter)
        binding.staffBlock.countTextView.setOnClickListener { navigateToStaffList(StaffType.EXCEPT_ACTOR) }
        binding.actorsBlock.countTextView.setOnClickListener { navigateToStaffList(StaffType.ACTOR) }
        binding.imagesBlock.countTextView.setOnClickListener { navigateToImages() }
        binding.similarBlock.allTextView.setOnClickListener { navigateToMovies() }
        binding.allSeriesTextView.setOnClickListener { navigateToSeriesInfo() }
    }

    private fun initActionsWithMovie() {
        binding.posterBlock.shareButton.setOnClickListener { shareLink() }
        binding.posterBlock.watchedButton.setOnClickListener {
            viewModel.addMovieToCollection(Constants.MOVIES_COLLECTION_WATCHED_ID)
        }
        binding.posterBlock.addToWatchButton.setOnClickListener {
            viewModel.addMovieToCollection(Constants.MOVIES_COLLECTION_TO_WATCH_ID)
        }
        binding.posterBlock.likeButton.setOnClickListener {
            viewModel.addMovieToCollection(Constants.MOVIES_COLLECTION_LIKES_ID)
        }
        binding.posterBlock.otherCommandsButton.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {
        viewModel.getCollectionToMoviesList(kinopoiskId)
        viewModel.collectionToMoviesLiveData.observe(viewLifecycleOwner) {
            collectionsAdapter.items = it
        }
        setBottomSheetVisibility(true)
        binding.helperView.setOnClickListener { setBottomSheetVisibility(false) }
        binding.closeImageView.setOnClickListener { setBottomSheetVisibility(false) }
    }

    private fun setMovieItemOnBottomSheet(model: MovieDetailModel) {
        binding.movieItem.apply {
            posterImageView.load(model.posterUrl)
            model.ratingKinopoisk?.let { ratingTextView.text = it.toString() }
            titleTextView.text = model.nameRu ?: model.nameEn ?: model.nameOriginal
            val yearGenre = if (model.genres.isNotEmpty()) {
                resources.getString(
                    R.string.movie_info_year_genres, model.year, model.genres.first().genre
                )
            } else ""
            yearGenreTextView.text = yearGenre
        }
    }

    private fun setBottomSheetVisibility(isVisible: Boolean) {
        binding.helperView.isVisible = isVisible
        if (isVisible) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.posterBlock.otherCommandsButton.setImageResource(R.drawable.other_blue)
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.posterBlock.otherCommandsButton.setImageResource(R.drawable.other_hor)
        }
    }

    private fun showDialogNewCollection() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialogBinding = DialogLayoutBinding.bind(dialogView)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()
        dialog.show()
        dialogBinding.closeImageView.setOnClickListener { dialog.dismiss() }
        dialogBinding.nameEditText.onTextChangedListener { text ->
            dialogBinding.okButton.isEnabled = text.isNotBlank()
        }
        dialogBinding.okButton.isEnabled = dialogBinding.nameEditText.text.toString().isNotBlank()
        dialogBinding.okButton.setOnClickListener {
            val name = dialogBinding.nameEditText.text.toString()
            viewModel.addNewCollection(name)
            dialog.dismiss()
        }
    }

    private fun shareLink() {
        val link = ApiSettings.generateShareLink(kinopoiskId)
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        requireActivity().startActivity(shareIntent)
        binding.posterBlock.shareButton.setImageResource(R.drawable.share_blue)
    }

    override fun onResume() {
        super.onResume()
        binding.posterBlock.shareButton.setImageResource(R.drawable.share)
    }

    private fun showImage(url: String, itemView: View) {
        binding.imageContainer.imageView.load(url)
        binding.imageContainer.container.isVisible = true
        showTransformation(true, itemView)
        binding.imageContainer.backgroundView.setOnClickListener {
            showTransformation(false, itemView)
            binding.imageContainer.container.visibility = View.GONE
        }
    }

    private fun showTransformation(isBeginning: Boolean, itemView: View) {
        val start = if (isBeginning) itemView
        else binding.imageContainer.imageView
        val end = if (isBeginning) binding.imageContainer.imageView
        else itemView
        val transform = MaterialContainerTransform().apply {
            startView = start
            endView = end
            addTarget(endView!!)
            setPathMotion(MaterialArcMotion())
            scrimColor = Color.TRANSPARENT
        }
        TransitionManager.beginDelayedTransition(binding.root, transform)
    }

    private fun navigateToSelf(kinopoiskId: Int, itemView: View) {
        val transitionName = getString(R.string.movie_transition_name)
        val extras = FragmentNavigatorExtras(itemView to transitionName)
        val action = MovieFragmentDirections.actionMovieFragment2Self(kinopoiskId)
        findNavController().navigate(action, extras)
    }

    private fun navigateToSeriesInfo() {
        val action = MovieFragmentDirections.actionMovieFragmentToSeriesFragment(
            filmName, kinopoiskId
        )
        findNavController().navigate(action)
    }

    private fun navigateToPersonInfo(personId: Int, itemView: View) {
        val transitionName = getString(R.string.person_transition_name)
        val extras = FragmentNavigatorExtras(itemView to transitionName)
        val action = MovieFragmentDirections.actionMovieFragmentToStaffFragment(personId)
        findNavController().navigate(action, extras)
    }

    private fun navigateToImages() {
        val action = MovieFragmentDirections.actionMovieFragmentToImagesFragment(kinopoiskId)
        findNavController().navigate(action)
    }

    private fun navigateToStaffList(type: StaffType) {
        val action =
            MovieFragmentDirections.actionMovieFragmentToStaffListFragment(kinopoiskId, type)
        findNavController().navigate(action)
    }

    private fun navigateToMovies() {
        val action = MovieFragmentDirections.actionMovieFragment2ToMoviesFragment2(
            collectionType = MovieCollectionType.SIMILAR, kinopoiskId = kinopoiskId
        )
        findNavController().navigate(action)
    }

    private fun showMovieInfo(info: MovieDetailPageModel) {
        filmName =
            info.movieDetail.nameRu ?: info.movieDetail.nameOriginal ?: info.movieDetail.nameEn
                    ?: ""
        binding.posterBlock.loadInfo(info.movieDetail)
        info.movieDetail.description?.let { description ->
            setDescription(description)
            fullDescription = description
        }
        info.movieDetail.shortDescription?.let {
            binding.shortDescriptionTextView.text = it
        } ?: {
            binding.shortDescriptionTextView.visibility = View.GONE
        }
        binding.actorsBlock.initBlock(info.actors)
        binding.staffBlock.initBlock(info.staff)
        if (info.gallery.count > 0) {
            binding.imagesBlock.isVisible = true
            binding.imagesBlock.initBlock(info.gallery)
        }
        if (info.similar.count > 0) {
            binding.similarBlock.isVisible = true
            binding.similarBlock.setSimilarFilms(info.similar)
        }
    }

    private fun setDescription(description: String) {
        if (description.length > DESCRIPTION_LIMIT) {
            description.takeLast(description.length - DESCRIPTION_LIMIT)
            val croppedDescription = description.take(DESCRIPTION_LIMIT) + "..."
            binding.descriptionTextView.text = croppedDescription
            isDescriptionCropped = true
        } else {
            binding.descriptionTextView.text = description
            isDescriptionCropped = false
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.mainContainer.isVisible = !isLoading
        binding.appBar.isVisible = !isLoading
        binding.toolbarWithBackIcon.isVisible = !isLoading
        binding.loaderBlock.isVisible = isLoading
    }

    private fun showSeriesInfo(model: SeriesInfoModel) {
        binding.seriesContainer.isVisible = true
        val title = getString(R.string.movie_info_season_series_title_text)
        binding.seriesTitleTextView.text = title
        val seasons = resources.getQuantityString(
            R.plurals.movie_info_seasons_with_quantity, model.seasonsCount, model.seasonsCount
        )
        val episodes = resources.getQuantityString(
            R.plurals.movie_info_episodes_with_quantity, model.episodesCount, model.episodesCount
        )
        binding.seasonsSeriesTextView.text =
            getString(R.string.movie_info_seasons_and_episodes_text, seasons, episodes)
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movieInfo.collect { movieInfo ->
                movieInfo ?: return@collect
                showMovieInfo(movieInfo)
                setMovieItemOnBottomSheet(movieInfo.movieDetail)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadingState.collect { showLoadingState(it) }
        }

        viewModel.seriesInfoLiveData.observe(viewLifecycleOwner) {
            showSeriesInfo(it)
        }

        viewModel.collectionIdsWithMovieLiveData.observe(viewLifecycleOwner) { mutableInfo ->
            binding.posterBlock.updateMovieMutableInfo(mutableInfo)
        }
    }


    companion object {
        private const val DESCRIPTION_LIMIT = 250

        const val KINOPOISK_ID_KEY = "kinopoisk_id"
    }
}