package com.al4apps.skillcinema.presentation.adapters.movies

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemShowAllBinding
import com.al4apps.skillcinema.domain.model.FooterModel
import com.al4apps.skillcinema.domain.model.HomeListModel
import com.al4apps.skillcinema.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class FooterAdapterDelegate(
    private val onShowAllClick: () -> Unit
) : AbsListItemAdapterDelegate<FooterModel, HomeListModel, FooterViewHolder>() {
    override fun isForViewType(
        item: HomeListModel,
        items: MutableList<HomeListModel>,
        position: Int
    ): Boolean {
        return item is FooterModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): FooterViewHolder {
        return FooterViewHolder(parent.inflate(R.layout.item_show_all, false), onShowAllClick)
    }

    override fun onBindViewHolder(
        item: FooterModel,
        holder: FooterViewHolder,
        payloads: MutableList<Any>
    ) {
    }
}

class FooterViewHolder(
    view: View,
    val onShowAllClick: () -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemShowAllBinding.bind(view)

    init {
        binding.nextButtonCardView.setOnClickListener { onShowAllClick() }
    }
}