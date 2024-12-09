package com.al4apps.skillcinema.presentation.searchsettings.years

data class Year(
    val year: Int,
    var isSelected: Boolean = false
)

data class YearsRange(
    val min: Year?,
    val max: Year?,
)
