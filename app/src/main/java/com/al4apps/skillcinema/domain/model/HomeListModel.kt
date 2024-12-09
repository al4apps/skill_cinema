package com.al4apps.skillcinema.domain.model

import androidx.annotation.StringRes

sealed class HomeListModel

class FooterModel(
    @StringRes val title: Int
) : HomeListModel()

