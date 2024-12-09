package com.al4apps.skillcinema.presentation.adapters.movies

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemMovieBinding
import com.al4apps.skillcinema.domain.model.MovieDbModel
import com.al4apps.skillcinema.domain.model.MoviesAndPersonsDb
import com.al4apps.skillcinema.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class MovieFromDbAdapterDelegate(
    private val onItemClick: (kinopoiskId: Int?, personId: Int?, itemView: View) -> Unit
) : AbsListItemAdapterDelegate<MovieDbModel, MoviesAndPersonsDb, MovieDbViewHolder>() {
    override fun isForViewType(
        item: MoviesAndPersonsDb,
        items: MutableList<MoviesAndPersonsDb>,
        position: Int
    ): Boolean {
        return item is MovieDbModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): MovieDbViewHolder {
        return MovieDbViewHolder(parent.inflate(R.layout.item_movie, false), onItemClick)
    }

    override fun onBindViewHolder(
        item: MovieDbModel,
        holder: MovieDbViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}

class MovieDbViewHolder(
    view: View,
    onItemClick: (kinopoiskId: Int?, personId: Int?, itemView: View) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMovieBinding.bind(view)
    private var id: Int? = null

    init {
        itemView.setOnClickListener { id?.let { onItemClick(it, null, itemView) } }
    }

    fun bind(movie: MovieDbModel) {
        id = movie.kinopoiskId
        itemView.transitionName = itemView.resources.getString(R.string.item_transition_name, id)
        binding.posterImageView.load(movie.posterUrl)
        binding.viewedImageView.isVisible = movie.isWatched
        binding.genreTextView.text = movie.genres.map { it.genre }.take(2).joinToString(", ")
        binding.watchedView.isVisible = movie.isWatched
        binding.titleTextView.text = movie.nameRu ?: movie.nameEn
        movie.ratingKinopoisk?.let {
            binding.imageCardView.isVisible = true
            binding.ratingTextView.text = it.toString()
        }
    }
}