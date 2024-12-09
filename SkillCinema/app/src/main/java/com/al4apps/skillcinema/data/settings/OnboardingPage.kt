package com.al4apps.skillcinema.data.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingPage(
    @StringRes
    val title: Int,
    @DrawableRes
    val image: Int,
)
