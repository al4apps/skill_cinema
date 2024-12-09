package com.al4apps.skillcinema.presentation.start

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.al4apps.skillcinema.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    @Inject
    lateinit var startViewModelFactory: StartViewModelFactory
    private val viewModel: StartViewModel by viewModels { startViewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.firstStartFlag.collect { isFirstStart ->
                navigateToNextPage(isFirstStart)
                SplashScreenObject.updateReadyStatus()
            }
        }
    }

    private fun navigateToNextPage(isFirstStart: Boolean) {
        if (isFirstStart) {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToOnboardingFragment())
        } else {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToMainFragment())
        }
    }
}
