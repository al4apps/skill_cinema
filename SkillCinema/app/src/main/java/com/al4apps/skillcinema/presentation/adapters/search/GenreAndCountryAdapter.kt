package com.al4apps.skillcinema.presentation.adapters.search

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemSearchFilterBinding
import com.al4apps.skillcinema.presentation.searchsettings.CountryUi
import com.al4apps.skillcinema.presentation.searchsettings.GenreOrCountryUi
import com.al4apps.skillcinema.presentation.searchsettings.GenreUi
import com.al4apps.skillcinema.utils.inflate
import com.al4apps.skillcinema.utils.themeColor
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class GenreAndCountryAdapter(
    onItemClick: (item: GenreOrCountryUi) -> Unit
) : AsyncListDifferDelegationAdapter<GenreOrCountryUi>(GenreAndCountryDiffUtil()) {

    init {
        delegatesManager.addDelegate(GenreDelegateAdapter(onItemClick))
            .addDelegate(CountryDelegateAdapter(onItemClick))
    }

    class GenreAndCountryDiffUtil : DiffUtil.ItemCallback<GenreOrCountryUi>() {
        override fun areItemsTheSame(
            oldItem: GenreOrCountryUi,
            newItem: GenreOrCountryUi
        ): Boolean {
            return when {
                oldItem is GenreUi && newItem is GenreUi -> oldItem.genreNumber == newItem.genreNumber
                oldItem is CountryUi && newItem is CountryUi -> oldItem.countryNumber == newItem.countryNumber
                else -> false
            }
        }

        override fun areContentsTheSame(
            oldItem: GenreOrCountryUi,
            newItem: GenreOrCountryUi
        ): Boolean {
            return oldItem == newItem
        }
    }
}

class CountryDelegateAdapter(
    private val onItemClick: (item: GenreOrCountryUi) -> Unit,
) : AbsListItemAdapterDelegate<CountryUi, GenreOrCountryUi, GenreAndCountryViewHolder>() {
    override fun isForViewType(
        item: GenreOrCountryUi,
        items: MutableList<GenreOrCountryUi>,
        position: Int
    ): Boolean {
        return item is CountryUi
    }

    override fun onCreateViewHolder(parent: ViewGroup): GenreAndCountryViewHolder {
        return GenreAndCountryViewHolder(
            parent.inflate(R.layout.item_search_filter, false),
            onItemClick,
        )
    }

    override fun onBindViewHolder(
        item: CountryUi,
        holder: GenreAndCountryViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}

class GenreDelegateAdapter(
    private val onItemClick: (item: GenreOrCountryUi) -> Unit,
) : AbsListItemAdapterDelegate<GenreUi, GenreOrCountryUi, GenreAndCountryViewHolder>() {

    override fun isForViewType(
        item: GenreOrCountryUi,
        items: MutableList<GenreOrCountryUi>,
        position: Int
    ): Boolean {
        return item is GenreUi
    }

    override fun onCreateViewHolder(parent: ViewGroup): GenreAndCountryViewHolder {
        return GenreAndCountryViewHolder(
            parent.inflate(R.layout.item_search_filter, false),
            onItemClick
        )
    }

    override fun onBindViewHolder(
        item: GenreUi,
        holder: GenreAndCountryViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

}

class GenreAndCountryViewHolder(
    view: View,
    onClick: (item: GenreOrCountryUi) -> Unit,
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemSearchFilterBinding.bind(view)
    private var item: GenreOrCountryUi? = null

    init {
        view.setOnClickListener {
            item?.let {
                it.isSelected = !it.isSelected
                setBackgroundColor(it)
                onClick(it)
            }
        }
    }

    fun bind(item: GenreOrCountryUi) {
        this.item = item
        setBackgroundColor(item)
        binding.itemNameTextView.text = item.name
    }

    private fun setBackgroundColor(item: GenreOrCountryUi) {
        if (item.isSelected) {
            binding.root.setBackgroundColor(itemView.context.themeColor(com.google.android.material.R.attr.colorSurfaceDim))
        } else {
            binding.root.setBackgroundColor(itemView.context.themeColor(com.google.android.material.R.attr.colorOnPrimary))
        }
    }
}
