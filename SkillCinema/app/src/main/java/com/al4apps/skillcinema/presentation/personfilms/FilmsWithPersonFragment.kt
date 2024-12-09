package com.al4apps.skillcinema.presentation.personfilms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentFilmsWithPersonBinding
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.presentation.adapters.movies.MoviesDelegationAdapter
import com.al4apps.skillcinema.presentation.movie.MovieFragment
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilmsWithPersonFragment :
    AbstractFragment<FragmentFilmsWithPersonBinding>(FragmentFilmsWithPersonBinding::inflate) {

    @Inject
    lateinit var filmsWithPersonViewModelFactory: FilmsWithPersonViewModelFactory
    private val viewModel: FilmsWithPersonViewModel by viewModels { filmsWithPersonViewModelFactory }
    private var filmsAdapter: MoviesDelegationAdapter by autoCleared()
    private val args: FilmsWithPersonFragmentArgs by navArgs()
    private var personId: Int = Constants.NO_ID_DEFAULT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        personId = if (args.personId != Constants.NO_ID_DEFAULT) args.personId
        else {
            arguments?.getInt(PERSON_ID_KEY) ?: Constants.NO_ID_DEFAULT
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        bindViewModel()
        viewModel.getPersonInfo(personId)
    }

    private fun initToolbar() {
        binding.toolbarInclude.toolbarWithBack.updateLayoutParamsByWindowInsetsTop()
        binding.toolbarInclude.toolbarWithBack.title =
            getString(R.string.staff_all_films_title_text)
        binding.toolbarInclude.toolbarWithBack.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        filmsAdapter = MoviesDelegationAdapter({ kinopoiskId, itemview ->
            navigateTomovie(itemview, kinopoiskId)
        }, {})
        binding.personFilmsRecyclerView.adapter = filmsAdapter
    }

    private fun navigateTomovie(itemView: View, kinopoiskId: Int) {
        val transitionName = getString(R.string.movie_transition_name)
        val extras = FragmentNavigatorExtras(itemView to transitionName)
        val bundle = bundleOf(MovieFragment.KINOPOISK_ID_KEY to kinopoiskId)
        val graphId = findNavController().graph.id
        val destination = findNavController().currentDestination?.id
        if (destination == R.id.filmsWithPersonFragment) {
            val action = R.id.movieFragment2
            findNavController().navigate(action, bundle, null, extras)
        }
        if (destination == R.id.filmsWithPersonFragment2) {
            val action = R.id.navigation_in_movie3
            findNavController().navigate(action, bundle, null, extras)
        }
    }

    private fun setupChips(professions: List<ProfessionOnFilm>) {
        binding.filmsFilterChipGroup.isVisible = true
        setupChipTitles(professions)

        binding.filmsFilterChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            val professionKey = when (checkedIds.first()) {
                binding.firstChip.id -> professions[0].professionKey
                binding.secondChip.id -> professions[1].professionKey
                binding.thirdChip.id -> professions[2].professionKey
                else -> professions[0].professionKey
            }
            viewModel.showFilmsWithProfession(professionKey)
        }
        if (binding.filmsFilterChipGroup.checkedChipId == View.NO_ID) {
            binding.firstChip.isChecked = true
        }
    }

    private fun setupChipTitles(professions: List<ProfessionOnFilm>) {
        binding.firstChip.text = getString(
            R.string.films_with_person_chip_text,
            getString(professions[0].professionKey.title),
            professions[0].filmsCount
        )
        if (professions.size > 1) {
            binding.secondChip.isVisible = true
            binding.secondChip.text = getString(
                R.string.films_with_person_chip_text,
                getString(professions[1].professionKey.title),
                professions[1].filmsCount
            )
        }
        if (professions.size > 2) {
            binding.thirdChip.isVisible = true
            binding.thirdChip.text = getString(
                R.string.films_with_person_chip_text,
                getString(professions[2].professionKey.title),
                professions[2].filmsCount
            )
        }
    }

    private fun bindViewModel() {
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) {}

        viewModel.personInfoLiveData.observe(viewLifecycleOwner) { person ->
            binding.staffNameTextView.text = person.nameRu ?: person.nameEn
        }

        viewModel.professionOnFilmLiveData.observe(viewLifecycleOwner) { professions ->
            setupChips(professions)
        }

        viewModel.filmsLiveData.observe(viewLifecycleOwner) {
            filmsAdapter.items = it
        }
    }

    companion object {
        const val PROFESSION_CHIPS_COUNT = 3
        const val PERSON_ID_KEY = "person_id"
    }
}