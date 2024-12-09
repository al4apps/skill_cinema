package com.al4apps.skillcinema.presentation.series

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.al4apps.skillcinema.databinding.FragmentSeriesBinding
import com.al4apps.skillcinema.presentation.adapters.series.SeriesDelegationAdapter
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SeriesFragment : AbstractFragment<FragmentSeriesBinding>(FragmentSeriesBinding::inflate) {

    @Inject
    lateinit var seriesViewModelFactory: SeriesViewModelFactory
    private val viewModel: SeriesViewModel by viewModels { seriesViewModelFactory }
    private var seriesAdapter: SeriesDelegationAdapter by autoCleared()
    private val args: SeriesFragmentArgs by navArgs()

    private var checkedChipNumber = View.NO_ID

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        bindViewModel()
        savedInstanceState?.let {
            checkedChipNumber = it.getInt(CHECKED_CHIP_KEY)
        }
        if (args.kinopoiskId != NO_ID_DEFAULT && savedInstanceState == null) {
            viewModel.getSeriesInfo(args.kinopoiskId)
        }
    }

    private fun initAdapter() {
        seriesAdapter = SeriesDelegationAdapter()
        binding.seasonsRecyclerView.adapter = seriesAdapter
        binding.seasonsRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initToolbar() {
        binding.seriesTitleToolbar.toolbarWithBack.updateLayoutParamsByWindowInsetsTop()
        binding.seriesTitleToolbar.toolbarWithBack.isTitleCentered = true
        binding.seriesTitleToolbar.toolbarWithBack.title = args.filmName
        binding.seriesTitleToolbar.toolbarWithBack.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initChipGroup(seasonsCount: Int) {
        for (i in 1..seasonsCount) {
            val chip = Chip(requireContext()).apply {
                isCheckable = true
                text = i.toString()
            }
            binding.filterChipGroup.addView(chip)
        }

        setCheckedChip()

        binding.filterChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val chip = view?.findViewById<Chip>(checkedIds.first())
                ?: return@setOnCheckedStateChangeListener
            checkedChipNumber = chip.text.toString().toInt()
            viewModel.showEpisodesBySeasons(checkedChipNumber)
        }
    }

    private fun setCheckedChip() {
        if (binding.filterChipGroup.checkedChipId == View.NO_ID) {
            val currentChip = if (checkedChipNumber == View.NO_ID) {
                val chip = (binding.filterChipGroup[0] as Chip)
                checkedChipNumber = chip.text.toString().toInt()
                chip
            } else {
                (binding.filterChipGroup[checkedChipNumber - 1] as Chip)
            }
            viewModel.showEpisodesBySeasons(checkedChipNumber)
            currentChip.isChecked = true
        }
    }

    private fun bindViewModel() {
        viewModel.seasonsLiveData.observe(viewLifecycleOwner) { initChipGroup(it) }
        viewModel.episodesLiveData.observe(viewLifecycleOwner) { list ->
            seriesAdapter.items = list
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CHECKED_CHIP_KEY, checkedChipNumber)
    }

    companion object {
        private const val NO_ID_DEFAULT = -1
        private const val CHECKED_CHIP_KEY = "checked_chip"
    }
}