package com.al4apps.skillcinema.domain.model

import androidx.annotation.StringRes

class StaffInfoModel(
    val staffList: List<StaffModel>,
    val type: StaffType,
    val count: Int,
    @StringRes
    val title: Int = type.title
)