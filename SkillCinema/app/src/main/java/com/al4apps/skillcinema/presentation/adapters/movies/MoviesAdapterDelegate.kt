package com.al4apps.skillcinema.presentation.adapters.movies

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemMovieBinding
import com.al4apps.skillcinema.domain.model.HomeListModel
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class MoviesAdapterDelegate(
    private val onItemClick: (kinopoiskId: Int, view: View) -> Unit
) : AbsListItemAdapterDelegate<MovieModel, HomeListModel, MoviesViewHolder>() {
    override fun isForViewType(
        item: HomeListModel,
        items: MutableList<HomeListModel>,
        position: Int
    ): Boolean {
        return item is MovieModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): MoviesViewHolder {
        return MoviesViewHolder(parent.inflate(R.layout.item_movie, false), onItemClick)
    }

    override fun onBindViewHolder(
        item: MovieModel,
        holder: MoviesViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}

class MoviesViewHolder(
    view: View,
    private val onItemClick: (kinopoiskId: Int, view: View) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMovieBinding.bind(view)
    private var id: Int? = null

    init {
        view.setOnClickListener { _ ->
            id?.let { onItemClick(it, itemView) }
        }
    }

    fun bind(movie: MovieModel) {
        itemView.transitionName = itemView.resources
            .getString(R.string.item_transition_name, movie.kinopoiskId)
        id = movie.kinopoiskId
        binding.titleTextView.text = movie.nameRu ?: movie.nameEn ?: ""
        binding.posterImageView.load(movie.posterUrlPreview) {
            placeholder(R.drawable.placeholder)
        }
        binding.watchedView.isVisible = movie.isWatched
        binding.viewedImageView.isVisible = movie.isWatched
        binding.genreTextView.text = movie.genres.map { it.genre }.take(2).joinToString(", ")
        movie.ratingKinopoisk?.let {
            binding.ratingCardView.isVisible = true
            binding.ratingTextView.text = it.toString()
        }
    }
}