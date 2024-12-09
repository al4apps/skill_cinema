package com.al4apps.skillcinema.presentation.adapters.series

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemEpisodeBinding
import com.al4apps.skillcinema.domain.model.EpisodeModel
import com.al4apps.skillcinema.domain.model.SeriesListModel
import com.al4apps.skillcinema.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class EpisodesAdapterDelegate :
    AbsListItemAdapterDelegate<EpisodeModel, SeriesListModel, EpisodeViewHolder>() {
    override fun isForViewType(
        item: SeriesListModel,
        items: MutableList<SeriesListModel>,
        position: Int
    ): Boolean {
        return item is EpisodeModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): EpisodeViewHolder {
        return EpisodeViewHolder(parent.inflate(R.layout.item_episode, false))
    }

    override fun onBindViewHolder(
        item: EpisodeModel,
        holder: EpisodeViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}

class EpisodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemEpisodeBinding.bind(view)
    fun bind(episode: EpisodeModel) {
        val episodeText = itemView.resources.getString(
            R.string.series_episode_number_with_title_text,
            episode.episodeNumber,
            episode.nameRu ?: episode.nameEn ?: ""
        )
        binding.episodeTextView.text = episodeText
        episode.releaseDate ?: return
        val releaseDate = episode.releaseDate.split("-").map { it.toInt() }
        if (releaseDate.size < 3) return

        val year = releaseDate[0]
        val monthNumber = releaseDate[1]
        val day = releaseDate[2]
        val monthName = getMonthName(monthNumber) ?: return
        val episodeDate = itemView.resources.getString(
            R.string.series_episode_info_text,
            day,
            monthName,
            year
        )
        binding.episodeInfoTextView.text = episodeDate
    }

    private fun getMonthName(monthNumber: Int): String? = when (monthNumber) {
        1 -> itemView.resources.getString(R.string.series_episode_month_1)
        2 -> itemView.resources.getString(R.string.series_episode_month_2)
        3 -> itemView.resources.getString(R.string.series_episode_month_3)
        4 -> itemView.resources.getString(R.string.series_episode_month_4)
        5 -> itemView.resources.getString(R.string.series_episode_month_5)
        6 -> itemView.resources.getString(R.string.series_episode_month_6)
        7 -> itemView.resources.getString(R.string.series_episode_month_7)
        8 -> itemView.resources.getString(R.string.series_episode_month_8)
        9 -> itemView.resources.getString(R.string.series_episode_month_9)
        10 -> itemView.resources.getString(R.string.series_episode_month_10)
        11 -> itemView.resources.getString(R.string.series_episode_month_11)
        12 -> itemView.resources.getString(R.string.series_episode_month_12)
        else -> null
    }
}