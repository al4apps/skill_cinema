package com.al4apps.skillcinema.presentation.adapters.series

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemSeasonBinding
import com.al4apps.skillcinema.domain.model.SeasonModel
import com.al4apps.skillcinema.domain.model.SeriesListModel
import com.al4apps.skillcinema.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class SeasonAdapterDelegate :
    AbsListItemAdapterDelegate<SeasonModel, SeriesListModel, SeasonViewHolder>() {
    override fun isForViewType(
        item: SeriesListModel,
        items: MutableList<SeriesListModel>,
        position: Int
    ): Boolean {
        return item is SeasonModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): SeasonViewHolder {
        return SeasonViewHolder(parent.inflate(R.layout.item_season, false))
    }

    override fun onBindViewHolder(
        item: SeasonModel,
        holder: SeasonViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}

class SeasonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemSeasonBinding.bind(view)
    fun bind(season: SeasonModel) {
        val episodesString = itemView.resources.getQuantityString(
            R.plurals.episodes_count_text,
            season.episodes.size,
            season.episodes.size
        )
        val text = itemView.resources.getString(
            R.string.series_item_season_text,
            season.number,
            episodesString
        )
        binding.seasonTextView.text = text
    }
}