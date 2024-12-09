package com.al4apps.skillcinema.presentation.main

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentMainBinding
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsBottom
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : AbstractFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }
    private lateinit var navController: NavController
    private var backStackCounter = START_BACKSTACK_COUNT
    private var lastDestinationId = 0
    private val sharingViewModel: SharingViewModel by activityViewModels()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backStackCounter -= 2
            navController.popBackStack()
        }
    }
    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.id != lastDestinationId) {
                lastDestinationId = destination.id
                backStackCounter++
            }
            if (backStackCounter > 2) {
                requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
            } else {
                onBackPressedCallback.remove()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavController()
        binding.bottomNavigationView.updateLayoutParamsByWindowInsetsBottom()
        viewModel
        observeLoadingState()
        savedInstanceState?.let {
            backStackCounter = it.getInt(BACKSTACK_COUNTER_KEY)
            lastDestinationId = it.getInt(DESTINATION_ID_KEY)
        }
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            sharingViewModel.loadingState.collect { isLoading ->
                showLoadingState(isLoading)
            }
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        binding.loaderBlock.isVisible = isLoading
        binding.bottomNavigationView.isVisible = !isLoading
        binding.mainNavHostContainer.isVisible = !isLoading
    }

    private fun initNavController() {
        navController =
            (childFragmentManager.findFragmentById(R.id.mainNavHostContainer) as NavHostFragment)
                .navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BACKSTACK_COUNTER_KEY, backStackCounter)
        outState.putInt(DESTINATION_ID_KEY, lastDestinationId)
    }

    override fun onDestroyView() {
        navController.removeOnDestinationChangedListener(onDestinationChangedListener)
        super.onDestroyView()
    }

    companion object {
        const val NO_ID_DEFAULT = -1

        private const val BACKSTACK_COUNTER_KEY = "backstack_key"
        private const val DESTINATION_ID_KEY = "destination_id"

        private const val START_BACKSTACK_COUNT = 1
    }
}