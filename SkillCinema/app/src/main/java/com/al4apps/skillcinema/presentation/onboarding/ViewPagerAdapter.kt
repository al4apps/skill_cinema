package com.al4apps.skillcinema.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.al4apps.skillcinema.data.settings.OnboardingPage

class ViewPagerAdapter(
    fragment: Fragment,
    private val pages: Array<OnboardingPage>,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        val page = pages[position]
        return OnboardingPageFragment.newInstance(
            page.title,
            page.image,
        )
    }
}