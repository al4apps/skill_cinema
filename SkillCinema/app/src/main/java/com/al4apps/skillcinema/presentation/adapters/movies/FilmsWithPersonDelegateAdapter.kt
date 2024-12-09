package com.al4apps.skillcinema.presentation.adapters.movies

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemMovieRightTextBinding
import com.al4apps.skillcinema.domain.model.FilmWithPersonModel
import com.al4apps.skillcinema.domain.model.HomeListModel
import com.al4apps.skillcinema.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class FilmsWithPersonDelegateAdapter(
    private val onItemClick: (kinopoiskId: Int, view: View) -> Unit,
) : AbsListItemAdapterDelegate<FilmWithPersonModel, HomeListModel, FilmWithPersonViewHolder>() {
    override fun isForViewType(
        item: HomeListModel,
        items: MutableList<HomeListModel>,
        position: Int
    ): Boolean {
        return item is FilmWithPersonModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): FilmWithPersonViewHolder {
        return FilmWithPersonViewHolder(
            parent.inflate(R.layout.item_movie_right_text, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(
        item: FilmWithPersonModel,
        holder: FilmWithPersonViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}

class FilmWithPersonViewHolder(
    view: View,
    onItemClick: (kinopoiskId: Int, view: View) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemMovieRightTextBinding.bind(view)
    private var kinopoiskId: Int? = null

    init {
        view.setOnClickListener { kinopoiskId?.let { onItemClick(it, itemView) } }
    }

    fun bind(film: FilmWithPersonModel) {
        kinopoiskId = film.filmId
        itemView.transitionName = itemView.resources.getString(R.string.item_transition_name, kinopoiskId)
        binding.posterImageView.load(R.drawable.poster_placeholder)
        film.rating?.let {
            binding.ratingCardView.isVisible = true
            binding.ratingTextView.text = it.toString()
        }
        binding.titleTextView.text = film.nameRu ?: film.nameEn
    }
}