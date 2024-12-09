package com.al4apps.skillcinema.presentation.stafflist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentStaffListBinding
import com.al4apps.skillcinema.domain.model.StaffType
import com.al4apps.skillcinema.presentation.adapters.StaffAdapter
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StaffListFragment :
    AbstractFragment<FragmentStaffListBinding>(FragmentStaffListBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: StaffListViewModelFactory
    private val viewModel: StaffListViewModel by viewModels { viewModelFactory }
    private val args: StaffListFragmentArgs by navArgs()
    private var staffAdapter: StaffAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        bindViewModel()
        viewModel.getStaff(args.kinopoiskId, args.staffType)
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.staffList.collectLatest {
                staffAdapter.submitList(it)
            }
        }
    }

    private fun initAdapter() {
        staffAdapter = StaffAdapter(isMatchParent = true, onItemClick = { staffId, itemView ->
            val transitionName = getString(R.string.person_transition_name)
            val extras = FragmentNavigatorExtras(itemView to transitionName)
            val action = StaffListFragmentDirections.actionStaffListFragmentToStaffFragment(staffId)
            findNavController().navigate(action, extras)
        })
        binding.staffListRecyclerView.adapter = staffAdapter
    }

    private fun initToolbar() {
        binding.toolbarInclude.toolbarWithBack.title = if (args.staffType == StaffType.ACTOR) {
            getString(R.string.staff_list_actors_list_title)
        } else {
            getString(R.string.staff_list_staff_title)
        }
        binding.toolbarInclude.toolbarWithBack.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.toolbarInclude.toolbarWithBack.updateLayoutParamsByWindowInsetsTop()
    }
}