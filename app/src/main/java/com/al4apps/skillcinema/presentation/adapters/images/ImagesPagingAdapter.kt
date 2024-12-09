package com.al4apps.skillcinema.presentation.adapters.images

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemImageBinding
import com.al4apps.skillcinema.databinding.ItemImageGridBinding
import com.al4apps.skillcinema.domain.model.ImageModel
import com.al4apps.skillcinema.utils.inflate

class ImagesPagingAdapter(
    private val onItemClick: (url: String, itemView: View) -> Unit,
) : PagingDataAdapter<ImageModel, ImageViewHolder>(ImagesDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = parent.inflate(R.layout.item_image_grid, false)
        val binding = ItemImageGridBinding.bind(view)
        if (viewType == 1) {
            binding.image.filmImageImageView.layoutParams.width *= 2
        }
        return ImageViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        image?.let { holder.bindGrid(it) }
    }

    override fun getItemViewType(position: Int): Int {
        return if ((position + 1) % 3 == 0) 1 else 2
    }
}