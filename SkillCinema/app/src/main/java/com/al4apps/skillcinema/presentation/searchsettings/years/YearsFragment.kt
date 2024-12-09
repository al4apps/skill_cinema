package com.al4apps.skillcinema.presentation.searchsettings.years

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentYearsBinding
import com.al4apps.skillcinema.presentation.adapters.search.YearsAdapter
import com.al4apps.skillcinema.presentation.search.SearchSharingViewModel
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.toast
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop

class YearsFragment : AbstractFragment<FragmentYearsBinding>(FragmentYearsBinding::inflate) {

    private val searchSharingViewModel: SearchSharingViewModel by activityViewModels()
    private var yearsAdapterMin: YearsAdapter by autoCleared()
    private var yearsAdapterMax: YearsAdapter by autoCleared()

    private val yearsViewModelFactory = YearsViewModelFactory()
    private val viewModel: YearsViewModel by viewModels { yearsViewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        getYearsRangeWithSavedYears()
        if (savedInstanceState == null) searchSharingViewModel.getSavedYearsRange()
        bindViewModel()
        initButtons()
    }

    private fun initToolbar() {
        binding.toolbarIncluded.toolbarWithBack.updateLayoutParamsByWindowInsetsTop()
        binding.toolbarIncluded.toolbarWithBack.isTitleCentered = true
        binding.toolbarIncluded.toolbarWithBack.setTitle(R.string.search_years_title)
        binding.toolbarIncluded.toolbarWithBack.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initButtons() {
        binding.tableRangeFrom.nextImageView.setOnClickListener { viewModel.nextRange(RANGE_TABLE_1) }
        binding.tableRangeFrom.prevImageView.setOnClickListener { viewModel.prevRange(RANGE_TABLE_1) }
        binding.tableRangeTo.nextImageView.setOnClickListener { viewModel.nextRange(RANGE_TABLE_2) }
        binding.tableRangeTo.prevImageView.setOnClickListener { viewModel.prevRange(RANGE_TABLE_2) }
        binding.saveButton.setOnClickListener {
            viewModel.saveYearsRange()
        }
    }

    private fun initAdapter() {
        yearsAdapterMin = YearsAdapter { item: Year ->
            viewModel.onItemClick(item, RANGE_TABLE_1)
        }
        binding.tableRangeFrom.yearsRecyclerView.apply {
            isVisible = true
            adapter = yearsAdapterMin
        }
        yearsAdapterMax = YearsAdapter { item: Year ->
            viewModel.onItemClick(item, RANGE_TABLE_2)
        }
        binding.tableRangeTo.yearsRecyclerView.apply {
            isVisible = true
            adapter = yearsAdapterMax
        }
    }

    private fun bindViewModel() {
        viewModel.years1LiveData.observe(viewLifecycleOwner) { years ->
            yearsAdapterMin.submitList(years)
            binding.tableRangeFrom.parameterNameTextView.text =
                getString(R.string.search_years_range_min_max, years.first().year, years.last().year)
        }
        viewModel.years2LiveData.observe(viewLifecycleOwner) {
            yearsAdapterMax.submitList(it)
            binding.tableRangeTo.parameterNameTextView.text =
                getString(R.string.search_years_range_min_max, it.first().year, it.last().year)
        }
        viewModel.itemToUnselectLiveData.observe(viewLifecycleOwner) {
            val index = yearsAdapterMin.currentList.indexOf(it)
            yearsAdapterMin.notifyItemChanged(index)
        }
        viewModel.itemToUnselect2LiveData.observe(viewLifecycleOwner) {
            val index = yearsAdapterMax.currentList.indexOf(it)
            yearsAdapterMax.notifyItemChanged(index)
        }
        viewModel.rangeLiveData.observe(viewLifecycleOwner) {
            searchSharingViewModel.saveYearsRange(it)
            findNavController().popBackStack()
        }
        viewModel.exceptionLiveData.observe(viewLifecycleOwner) { exception ->
            if (exception == EXCEPTION_INCORRECT_RANGE) {
                toast(R.string.search_years_range_incorrect_toast)
            }
        }
    }

    private fun getYearsRangeWithSavedYears() {
        searchSharingViewModel.yearsLiveData.observe(viewLifecycleOwner) {
            viewModel.setSelectedYears(it)
        }
    }

    companion object {
        const val RANGE_TABLE_1 = 1
        const val RANGE_TABLE_2 = 2

        const val EXCEPTION_INCORRECT_RANGE = 21
    }
}