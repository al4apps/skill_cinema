package com.al4apps.skillcinema.presentation.movie

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ImagesBlockViewBinding
import com.al4apps.skillcinema.domain.model.GalleryModel
import com.al4apps.skillcinema.presentation.adapters.images.ImagesAdapter
import com.al4apps.skillcinema.presentation.decor.ItemOffsetDecoration

class ImagesBlockView(
    private val context: Context,
    attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {
    private var binding: ImagesBlockViewBinding
    private lateinit var imagesAdapter: ImagesAdapter
    var countTextView: TextView
        private set

    init {
        val inflatedView = inflate(context, R.layout.images_block_view, this)
        binding = ImagesBlockViewBinding.bind(inflatedView)
        countTextView = binding.titleBlock.countTextView
    }

    fun setAdapter(adapter: ImagesAdapter) {
        imagesAdapter = adapter
        binding.imagesRecyclerView.adapter = adapter
        binding.imagesRecyclerView.addItemDecoration(ItemOffsetDecoration(context))
    }

    fun initBlock(gallery: GalleryModel) {
        binding.titleBlock.titleTextView.setText(R.string.movie_info_images_block_title)
        if (gallery.list.isNotEmpty()) {
            binding.titleBlock.countTextView.text =
                resources.getString(R.string.movie_info_item_count, gallery.count)
        }
        imagesAdapter.submitList(gallery.list)
    }
}