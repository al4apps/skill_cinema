package com.al4apps.skillcinema.presentation.staff

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentPersonBinding
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.model.PersonModel
import com.al4apps.skillcinema.presentation.adapters.movies.MoviesDelegationAdapter
import com.al4apps.skillcinema.presentation.movie.MovieFragment
import com.al4apps.skillcinema.presentation.personfilms.FilmsWithPersonFragment
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.themeColor
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PersonFragment : AbstractFragment<FragmentPersonBinding>(FragmentPersonBinding::inflate) {

    @Inject
    lateinit var personViewModelFactory: PersonViewModelFactory
    private val viewModel: PersonViewModel by viewModels { personViewModelFactory }
    private val args: PersonFragmentArgs by navArgs()
    private var moviesAdapter: MoviesDelegationAdapter by autoCleared()
    private var personId = Constants.NO_ID_DEFAULT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.mainNavHostContainer
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(com.google.android.material.R.attr.colorSurface))
        }
        personId = if (args.personId != Constants.NO_ID_DEFAULT) args.personId
        else arguments?.getInt(PERSON_ID_KEY) ?: Constants.NO_ID_DEFAULT
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        initViews()
        initAdapter()
        if (savedInstanceState == null) getPersonInfo()
        bindViewModel()
        initRoutes()
    }

    private fun initRoutes() {
        binding.titleBlock.countTextView.setOnClickListener {
            val bundle = bundleOf(FilmsWithPersonFragment.PERSON_ID_KEY to personId)
            val destinationId = findNavController().currentDestination?.id
            if (destinationId == R.id.staffFragment) {
                val action = R.id.filmsWithPersonFragment
                findNavController().navigate(action, bundle)
            }
            if (destinationId == R.id.personFragment) {
                val action = R.id.filmsWithPersonFragment2
                findNavController().navigate(action, bundle)
            }
        }
    }

    private fun initAdapter() {
        moviesAdapter = MoviesDelegationAdapter(onItemClick = { kinopoiskId, itemView ->
            navigateToMovie(itemView, kinopoiskId)
        }, {})
        binding.bestWorksBlockView.setMoviesAdapter(moviesAdapter)
    }

    private fun navigateToMovie(itemView: View, kinopoiskId: Int) {
        val transitionName = getString(R.string.movie_transition_name)
        val extras = FragmentNavigatorExtras(itemView to transitionName)
        val bundle = bundleOf(MovieFragment.KINOPOISK_ID_KEY to kinopoiskId)

        val destinationId = findNavController().currentDestination?.id
        val action = if (destinationId == R.id.staffFragment) {
            R.id.movieFragment2
        } else {
            R.id.navigation_in_movie3
        }
        findNavController().navigate(action, bundle, null, extras)
    }

    private fun initViews() {
        binding.includedToolbar.toolbarWithBack.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.includedToolbar.toolbarWithBack.updateLayoutParamsByWindowInsetsTop()
        binding.titleBlock.titleTextView.setText(R.string.staff_all_films_title_text)
        binding.titleBlock.countTextView.setText(R.string.staff_to_films_button_text)
    }

    private fun getPersonInfo() {
        if (personId == Constants.NO_ID_DEFAULT) return
        viewModel.getPersonInfo(personId)
    }

    private fun setPersonInfo(person: PersonModel) {
        binding.appCompatImageView.load(person.posterUrl)
        binding.staffNameTextView.text = person.nameRu ?: person.nameEn ?: ""
        binding.staffProfessionTextView.text = person.profession
        val filmsCountText = resources.getQuantityString(
            R.plurals.person_films_count_text, person.films.size, person.films.size
        )
        binding.filmsCountTextView.text = filmsCountText
        binding.appCompatImageView.setOnClickListener {
            showImage(person.posterUrl)
        }
    }

    private fun showImage(posterUrl: String) {
        binding.imageContainer.container.isVisible = true
        binding.imageContainer.imageView.load(posterUrl)
        binding.imageContainer.container.setOnClickListener {
            binding.imageContainer.container.visibility = View.GONE
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        binding.linearProgress.isVisible = isLoading
    }

    private fun bindViewModel() {
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner, ::updateLoadingState)
        viewModel.personLiveData.observe(viewLifecycleOwner, ::setPersonInfo)
        viewModel.bestFilmsLiveData.observe(viewLifecycleOwner) { collection ->
            binding.bestWorksBlockView.setMoviesCollection(collection)
        }
    }

    companion object {
        const val PERSON_ID_KEY = "person_id"

    }
}