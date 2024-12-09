package com.al4apps.skillcinema.presentation.adapters.movies

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemMovieBinding
import com.al4apps.skillcinema.domain.model.HomeListModel
import com.al4apps.skillcinema.domain.model.SimilarFilmModel
import com.al4apps.skillcinema.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class SimilarFilmsAdapterDelegate(
    private val onItemClick: (kinopoiskId: Int, view: View) -> Unit
) : AbsListItemAdapterDelegate<SimilarFilmModel, HomeListModel, SimilarFilmsViewHolder>() {
    override fun isForViewType(
        item: HomeListModel,
        items: MutableList<HomeListModel>,
        position: Int
    ): Boolean {
        return item is SimilarFilmModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): SimilarFilmsViewHolder {
        return SimilarFilmsViewHolder(parent.inflate(R.layout.item_movie, false), onItemClick)
    }

    override fun onBindViewHolder(
        item: SimilarFilmModel,
        holder: SimilarFilmsViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}

class SimilarFilmsViewHolder(
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

    fun bind(movie: SimilarFilmModel) {
        id = movie.filmId
        itemView.transitionName = itemView.resources.getString(R.string.item_transition_name, id)
        binding.titleTextView.text = movie.nameRu
        binding.posterImageView.load(movie.posterUrlPreview)
    }
}