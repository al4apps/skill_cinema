package com.al4apps.skillcinema.presentation.adapters.images

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemImageBinding
import com.al4apps.skillcinema.databinding.ItemImageGridBinding
import com.al4apps.skillcinema.domain.model.ImageModel
import com.al4apps.skillcinema.utils.inflate

class ImagesAdapter(
    private val onItemClick: (url: String, itemView: View) -> Unit
) : ListAdapter<ImageModel, ImageViewHolder>(ImagesDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(parent.inflate(R.layout.item_image, false), onItemClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindLinear(currentList[position])
    }
}
class ImagesDiffUtil : DiffUtil.ItemCallback<ImageModel>() {
    override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
        return oldItem.imageUrl == newItem.imageUrl
    }

    override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
        return oldItem == newItem
    }
}

class ImageViewHolder(
    private val view: View,
    onItemClick: (url: String, itemView: View) -> Unit
    ) : RecyclerView.ViewHolder(view) {
    private var imageUrl: String? = null
    private lateinit var imageView: ImageView

    init {
        view.setOnClickListener { imageUrl?.let { onItemClick(it, imageView) } }
    }

    fun bindLinear(imageModel: ImageModel) {
        imageUrl = imageModel.imageUrl
        itemView.transitionName = imageUrl
        val binding = ItemImageBinding.bind(view)
        imageView = binding.filmImageImageView
        binding.filmImageImageView.load(imageModel.imageUrl)
    }

    fun bindGrid(imageModel: ImageModel) {
        imageUrl = imageModel.imageUrl
        itemView.transitionName = imageUrl
        val binding = ItemImageGridBinding.bind(view)
        imageView = binding.image.filmImageImageView
        binding.image.filmImageImageView.load(imageModel.imageUrl)
    }
}