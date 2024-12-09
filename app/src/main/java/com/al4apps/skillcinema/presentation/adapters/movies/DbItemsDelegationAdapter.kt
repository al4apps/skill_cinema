package com.al4apps.skillcinema.presentation.adapters.movies

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.al4apps.skillcinema.domain.model.MovieDbModel
import com.al4apps.skillcinema.domain.model.MoviesAndPersonsDb
import com.al4apps.skillcinema.domain.model.PersonDbModel
import com.al4apps.skillcinema.presentation.adapters.collections.PersonDbAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class DbItemsDelegationAdapter(
    onItemClick: (kinopoiskId: Int?, personId: Int?, view: View) -> Unit,
) : AsyncListDifferDelegationAdapter<MoviesAndPersonsDb>(ModelDiffUtil()) {

    init {
        delegatesManager.addDelegate(MovieFromDbAdapterDelegate(onItemClick))
            .addDelegate(PersonDbAdapterDelegate(onItemClick))
    }

    class ModelDiffUtil : DiffUtil.ItemCallback<MoviesAndPersonsDb>() {
        override fun areItemsTheSame(oldItem: MoviesAndPersonsDb, newItem: MoviesAndPersonsDb): Boolean {
            return when {
                oldItem is MovieDbModel && newItem is MovieDbModel -> oldItem.kinopoiskId == newItem.kinopoiskId
                oldItem is PersonDbModel && newItem is PersonDbModel -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: MoviesAndPersonsDb, newItem: MoviesAndPersonsDb): Boolean {
            return oldItem == newItem
        }

    }
}
