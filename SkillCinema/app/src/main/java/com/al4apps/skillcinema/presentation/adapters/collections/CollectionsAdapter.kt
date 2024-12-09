package com.al4apps.skillcinema.presentation.adapters.collections

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemCollectionBinding
import com.al4apps.skillcinema.domain.Constants
import com.al4apps.skillcinema.domain.model.Collection
import com.al4apps.skillcinema.utils.inflate

class CollectionsAdapter(
    private val onItemClick: (collectionId: Int) -> Unit,
    private val onDeleteItemClick: (collectionId: Int) -> Unit,
) : ListAdapter<Collection, CollectionsViewHolder>(CollectionsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        return CollectionsViewHolder(
            parent.inflate(R.layout.item_collection, false),
            onItemClick,
            onDeleteItemClick
        )
    }

    override fun onBindViewHolder(holder: CollectionsViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class CollectionsDiffUtil : DiffUtil.ItemCallback<Collection>() {
        override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
            return oldItem == newItem
        }
    }
}

class CollectionsViewHolder(
    view: View,
    private val onItemClick: (collectionId: Int) -> Unit,
    private val onDeleteItemClick: (collectionId: Int) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemCollectionBinding.bind(view)
    private var id: Int? = null

    init {
        itemView.setOnClickListener { id?.let { onItemClick(it) } }
    }

    fun bind(collection: Collection) {
        id = collection.id
        itemView.transitionName = itemView.resources.getString(R.string.item_transition_name, id)
        binding.collectionImageView.setImageResource(collection.drawableRes)
        val name = when (collection.id) {
            Constants.MOVIES_COLLECTION_TO_WATCH_ID -> {
                itemView.resources.getString(R.string.profile_collection_to_watch_name)
            }
            Constants.MOVIES_COLLECTION_LIKES_ID -> {
                itemView.resources.getString(R.string.profile_collection_likes_name)
            }
            else -> collection.name
        }
        binding.deleteButtonImageView.isVisible = collection.isUsersCollection
        binding.deleteButtonImageView.setOnClickListener {
            onDeleteItemClick(collection.id)
        }
        binding.nameTextView.text = name
        binding.countTextView.text = collection.count.toString()
    }
}