package com.al4apps.skillcinema.presentation.adapters.movies

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemMovieBinding
import com.al4apps.skillcinema.databinding.ItemMovieRightTextBinding
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.utils.inflate

class MoviesPagingAdapter(
    private val isTextOnRight: Boolean = false,
    private val onItemClick: (kinopoiskId: Int, view: View) -> Unit
) : PagingDataAdapter<MovieModel, MoviesVerticalViewHolder>(MovieDiffUtil()) {
    override fun onBindViewHolder(holder: MoviesVerticalViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(item) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MoviesVerticalViewHolder {
        val view = if (isTextOnRight) {
            parent.inflate(R.layout.item_movie_right_text, false)
        } else {
            parent.inflate(R.layout.item_movie, false)

        }
        return MoviesVerticalViewHolder(view, onItemClick, isTextOnRight)
    }

}

class MovieDiffUtil : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem.kinopoiskId == newItem.kinopoiskId
    }

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem == newItem
    }
}

class MoviesVerticalViewHolder(
    view: View,
    private val onItemClick: (kinopoiskId: Int, view: View) -> Unit,
    isTextOnRight: Boolean
) : RecyclerView.ViewHolder(view) {
    private val binding = if (isTextOnRight) ItemMovieRightTextBinding.bind(view)
    else ItemMovieBinding.bind(view)

    fun bind(movie: MovieModel) {
        itemView.transitionName = itemView.resources.getString(
            R.string.item_transition_name,
            movie.kinopoiskId
        )
        binding.root.setOnClickListener { _ ->
            onItemClick(movie.kinopoiskId, itemView)
        }
        if (binding is ItemMovieBinding) {
            binding.titleTextView.text = movie.nameRu
            binding.posterImageView.load(movie.posterUrlPreview)
            binding.genreTextView.text = movie.genres.map { it.genre }.take(2).joinToString(", ")
            movie.ratingKinopoisk?.let {
                binding.ratingCardView.isVisible = true
                binding.ratingTextView.text = it.toString()
            }
        }
        if (binding is ItemMovieRightTextBinding) {
            binding.posterImageView.load(movie.posterUrlPreview)
            binding.titleTextView.text = movie.nameRu
            val yearGenre = if (movie.genres.isNotEmpty()) {
                itemView.resources.getString(
                    R.string.movie_info_year_genres, movie.year, movie.genres.first().genre
                )
            } else ""
            binding.yearGenreTextView.text = yearGenre
            movie.ratingKinopoisk?.let {
                binding.ratingCardView.isVisible = true
                binding.ratingTextView.text = it.toString()
            }
        }
    }
}