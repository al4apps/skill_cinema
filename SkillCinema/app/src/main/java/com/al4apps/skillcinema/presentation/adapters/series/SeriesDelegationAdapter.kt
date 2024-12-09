package com.al4apps.skillcinema.presentation.adapters.series

import androidx.recyclerview.widget.DiffUtil
import com.al4apps.skillcinema.domain.model.EpisodeModel
import com.al4apps.skillcinema.domain.model.SeasonModel
import com.al4apps.skillcinema.domain.model.SeriesListModel
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class SeriesDelegationAdapter :
    AsyncListDifferDelegationAdapter<SeriesListModel>(SeriesDiffUtil()) {

    init {
        delegatesManager.addDelegate(EpisodesAdapterDelegate())
            .addDelegate(SeasonAdapterDelegate())
    }
}

class SeriesDiffUtil : DiffUtil.ItemCallback<SeriesListModel>() {
    override fun areItemsTheSame(oldItem: SeriesListModel, newItem: SeriesListModel): Boolean {
        return when {
            oldItem is EpisodeModel && newItem is EpisodeModel -> oldItem.episodeNumber == newItem.episodeNumber
            oldItem is SeasonModel && newItem is SeasonModel -> oldItem.number == newItem.number
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: SeriesListModel, newItem: SeriesListModel): Boolean {
        return oldItem == newItem
    }
}