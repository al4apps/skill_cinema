package com.al4apps.skillcinema.presentation.adapters.movies

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.utils.inflate

class MoviesVerticalAdapter(
    private val onItemClick: (kinopoiskId: Int, view: View) -> Unit
) : ListAdapter<MovieModel, MoviesVerticalViewHolder>(MovieDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesVerticalViewHolder {
        return MoviesVerticalViewHolder(
            parent.inflate(R.layout.item_movie, false), onItemClick, false
        )
    }

    override fun onBindViewHolder(holder: MoviesVerticalViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}