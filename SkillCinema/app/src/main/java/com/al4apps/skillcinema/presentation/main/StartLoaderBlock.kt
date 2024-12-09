package com.al4apps.skillcinema.presentation.main

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.al4apps.skillcinema.R

class StartLoaderBlock constructor(
    context: Context,
    attrs: AttributeSet
): ConstraintLayout(context, attrs) {

    init {
        inflate(context, R.layout.start_loader_block, this)
    }
}