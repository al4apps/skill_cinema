package com.al4apps.skillcinema.presentation.adapters.collections

import androidx.recyclerview.widget.DiffUtil
import com.al4apps.skillcinema.domain.model.CollectionFooter
import com.al4apps.skillcinema.domain.model.CollectionHeader
import com.al4apps.skillcinema.domain.model.CollectionListModel
import com.al4apps.skillcinema.domain.model.CollectionToMovie
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class CollectionsListDelegationAdapter(
    onAddCollectionClick: () -> Unit,
    onCheckboxClick: (id: Int) -> Unit
) : AsyncListDifferDelegationAdapter<CollectionListModel>(CollectionsListDiffUtil()) {

    init {
        delegatesManager.addDelegate(CollectionHeaderAdapterDelegate())
            .addDelegate(CollectionListAdapterDelegate(onCheckboxClick))
            .addDelegate(CollectionFooterAdapterDelegate(onAddCollectionClick))
    }

    class CollectionsListDiffUtil : DiffUtil.ItemCallback<CollectionListModel>() {
        override fun areItemsTheSame(
            oldItem: CollectionListModel,
            newItem: CollectionListModel
        ): Boolean {
            return when {
                oldItem is CollectionHeader && newItem is CollectionHeader -> oldItem == newItem
                oldItem is CollectionFooter && newItem is CollectionFooter -> oldItem == newItem
                oldItem is CollectionToMovie && newItem is CollectionToMovie -> {
                    oldItem.collection.id == newItem.collection.id
                }
                else -> false
            }
        }

        override fun areContentsTheSame(
            oldItem: CollectionListModel,
            newItem: CollectionListModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}