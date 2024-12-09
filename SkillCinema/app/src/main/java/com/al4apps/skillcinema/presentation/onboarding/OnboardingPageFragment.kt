package com.al4apps.skillcinema.presentation.onboarding

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.al4apps.skillcinema.databinding.FragmentOnboardingPageBinding
import com.al4apps.skillcinema.utils.AbstractFragment

class OnboardingPageFragment :
    AbstractFragment<FragmentOnboardingPageBinding>(FragmentOnboardingPageBinding::inflate) {
    private var title: Int? = null
    private var image: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getInt(TITLE_KEY)
            image = it.getInt(IMAGE_KEY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title?.let { binding.titleTextView.setText(it) }
        image?.let { binding.imageView.setImageResource(it) }
    }

    companion object {
        private const val TITLE_KEY = "title_key"
        private const val IMAGE_KEY = "image_key"
        fun newInstance(
            @StringRes titleRes: Int,
            @DrawableRes imageRes: Int,
        ) = OnboardingPageFragment().apply {
            arguments = Bundle().apply {
                putInt(TITLE_KEY, titleRes)
                putInt(IMAGE_KEY, imageRes)
            }
        }
    }
}
