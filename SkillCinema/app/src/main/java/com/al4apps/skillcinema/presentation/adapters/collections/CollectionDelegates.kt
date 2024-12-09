package com.al4apps.skillcinema.presentation.adapters.collections

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemCollectionCheckboxBinding
import com.al4apps.skillcinema.databinding.ItemCollectionFooterBinding
import com.al4apps.skillcinema.databinding.ItemCollectionHeaderBinding
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.model.CollectionFooter
import com.al4apps.skillcinema.domain.model.CollectionHeader
import com.al4apps.skillcinema.domain.model.CollectionListModel
import com.al4apps.skillcinema.domain.model.CollectionToMovie
import com.al4apps.skillcinema.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class CollectionHeaderAdapterDelegate :
    AbsListItemAdapterDelegate<CollectionHeader, CollectionListModel, CollectionHeaderViewHolder>() {
    override fun isForViewType(
        item: CollectionListModel, items: MutableList<CollectionListModel>, position: Int
    ): Boolean {
        return item is CollectionHeader
    }

    override fun onCreateViewHolder(parent: ViewGroup): CollectionHeaderViewHolder {
        return CollectionHeaderViewHolder(parent.inflate(R.layout.item_collection_header, false))
    }

    override fun onBindViewHolder(
        item: CollectionHeader, holder: CollectionHeaderViewHolder, payloads: MutableList<Any>
    ) {
        val binding = ItemCollectionHeaderBinding.bind(holder.itemView)
        binding.nameTextView.setText(item.nameRes)
        binding.folderImageView.setImageResource(item.iconRes)
    }
}

class CollectionHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

class CollectionFooterAdapterDelegate(
    private val onAddCollectionClick: () -> Unit,
) : AbsListItemAdapterDelegate<CollectionFooter, CollectionListModel, CollectionHeaderViewHolder>() {
    override fun isForViewType(
        item: CollectionListModel, items: MutableList<CollectionListModel>, position: Int
    ): Boolean {
        return item is CollectionFooter
    }

    override fun onCreateViewHolder(parent: ViewGroup): CollectionHeaderViewHolder {
        return CollectionHeaderViewHolder(parent.inflate(R.layout.item_collection_footer, false))
    }

    override fun onBindViewHolder(
        item: CollectionFooter, holder: CollectionHeaderViewHolder, payloads: MutableList<Any>
    ) {
        val binding = ItemCollectionFooterBinding.bind(holder.itemView)
        binding.nameTextView.setText(item.nameRes)
        binding.plusImageView.setImageResource(item.iconRes)
        binding.footerLayout.setOnClickListener { onAddCollectionClick() }
    }
}

class CollectionListAdapterDelegate(
    private val onCheckBoxClick: (id: Int) -> Unit
) : AbsListItemAdapterDelegate<CollectionToMovie, CollectionListModel, CollectionListViewHolder>() {
    override fun isForViewType(
        item: CollectionListModel, items: MutableList<CollectionListModel>, position: Int
    ): Boolean {
        return item is CollectionToMovie
    }

    override fun onCreateViewHolder(parent: ViewGroup): CollectionListViewHolder {
        return CollectionListViewHolder(
            parent.inflate(R.layout.item_collection_checkbox, false), onCheckBoxClick
        )
    }

    override fun onBindViewHolder(
        item: CollectionToMovie, holder: CollectionListViewHolder, payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

}

class CollectionListViewHolder(
    view: View, private val onCheckBoxClick: (id: Int) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemCollectionCheckboxBinding.bind(view)

    fun bind(item: CollectionToMovie) {
        val name = when (item.collection.id) {
            Constants.MOVIES_COLLECTION_TO_WATCH_ID -> {
                itemView.resources.getString(R.string.profile_collection_to_watch_name)
            }
            Constants.MOVIES_COLLECTION_LIKES_ID -> {
                itemView.resources.getString(R.string.profile_collection_likes_name)
            }
            else -> item.collection.name
        }

        binding.nameTextView.text = name
        binding.countTextView.text = item.collection.count.toString()
        updateCheckbox(item.isMovieInCollection)
        binding.checkbox.setOnClickListener {
            onCheckBoxClick(item.collection.id)
        }
    }

    private fun updateCheckbox(isInCollection: Boolean) {
        binding.checkbox.setImageResource(
            if (isInCollection) R.drawable.filled_checkbox
            else R.drawable.empty_checkbox
        )
    }
}