package com.al4apps.skillcinema.presentation.adapters.movies

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.al4apps.skillcinema.domain.model.FilmWithPersonModel
import com.al4apps.skillcinema.domain.model.HomeListModel
import com.al4apps.skillcinema.domain.model.MovieModel
import com.al4apps.skillcinema.domain.model.SimilarFilmModel
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class MoviesDelegationAdapter(
    onItemClick: (kinopoiskId: Int, view: View) -> Unit,
    onShowAllClick: () -> Unit,
) : AsyncListDifferDelegationAdapter<HomeListModel>(ModelDiffUtil()) {

    init {
        delegatesManager.addDelegate(MoviesAdapterDelegate(onItemClick))
            .addDelegate(FooterAdapterDelegate(onShowAllClick))
            .addDelegate(SimilarFilmsAdapterDelegate(onItemClick))
            .addDelegate(FilmsWithPersonDelegateAdapter(onItemClick))
    }

    class ModelDiffUtil : DiffUtil.ItemCallback<HomeListModel>() {
        override fun areItemsTheSame(oldItem: HomeListModel, newItem: HomeListModel): Boolean {
            return when {
                oldItem is MovieModel && newItem is MovieModel -> oldItem.kinopoiskId == newItem.kinopoiskId
                oldItem is SimilarFilmModel && newItem is SimilarFilmModel -> oldItem.filmId == newItem.filmId
                oldItem is FilmWithPersonModel && newItem is FilmWithPersonModel -> oldItem.filmId == newItem.filmId
                else -> true
            }
        }

        override fun areContentsTheSame(oldItem: HomeListModel, newItem: HomeListModel): Boolean {
            return oldItem == newItem
        }

    }
}
