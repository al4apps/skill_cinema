package com.al4apps.skillcinema.presentation.onboarding

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.al4apps.skillcinema.data.settings.Resources
import com.al4apps.skillcinema.databinding.FragmentOnboardingBinding
import com.al4apps.skillcinema.utils.AbstractFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingFragment :
    AbstractFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {
    private lateinit var onboardingAdapter: ViewPagerAdapter
    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.continueButton.isVisible = position == LAST_PAGE_POSITION
            binding.continueButton.isEnabled = position == LAST_PAGE_POSITION
        }
    }

    @Inject
    lateinit var onboardingViewModelFactory: OnboardingViewModelFactory
    private val viewModel: OnboardingViewModel by viewModels { onboardingViewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewPagerAdapter()
        binding.viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        binding.skipTextView.setOnClickListener { navigateToMainFragment() }
        binding.continueButton.setOnClickListener { navigateToMainFragment() }
        viewModel.setFirstStartFlag()
    }

    private fun navigateToMainFragment() {
        findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToMainFragment())
    }

    private fun setViewPagerAdapter() {
        onboardingAdapter = ViewPagerAdapter(this, Resources.onboardingPages)
        binding.viewPager.adapter = onboardingAdapter
        binding.dotsIndicator.attachTo(binding.viewPager)
    }

    override fun onStop() {
        super.onStop()
        binding.viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    companion object {
        private const val LAST_PAGE_POSITION = 2
    }
}
