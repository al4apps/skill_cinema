package com.al4apps.skillcinema.presentation.images

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.FragmentImagesBinding
import com.al4apps.skillcinema.domain.model.ImageType
import com.al4apps.skillcinema.domain.model.QueryImages
import com.al4apps.skillcinema.presentation.adapters.images.ImagesPagingAdapter
import com.al4apps.skillcinema.presentation.decor.ItemOffsetDecoration
import com.al4apps.skillcinema.utils.AbstractFragment
import com.al4apps.skillcinema.utils.autoCleared
import com.al4apps.skillcinema.utils.toast
import com.al4apps.skillcinema.utils.updateLayoutParamsByWindowInsetsTop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImagesFragment : AbstractFragment<FragmentImagesBinding>(FragmentImagesBinding::inflate) {

    private val args: ImagesFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ImagesViewModelFactory
    private val viewModel: ImagesViewModel by viewModels { viewModelFactory }
    private var imagesAdapter: ImagesPagingAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        setupChips()
        bindViewModel()
    }

    private fun setupChips() {
        binding.filterChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            val imageType = when (checkedIds.first()) {
                binding.stillImagesChip.id -> ImageType.STILL
                binding.shootingImagesChip.id -> ImageType.SHOOTING
                binding.postersChip.id -> ImageType.POSTER
                else -> ImageType.STILL
            }
            viewModel.setQuery(QueryImages(args.kinopoiskId, imageType))
        }
        if (binding.filterChipGroup.checkedChipId == View.NO_ID) {
            binding.stillImagesChip.isChecked = true
        }
    }

    private fun initAdapter() {
        imagesAdapter = ImagesPagingAdapter { url, itemView ->
            showImage(url, itemView)
        }
        binding.imagesListRecyclerView.adapter = imagesAdapter
        binding.imagesListRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.imagesListRecyclerView.addItemDecoration(ItemOffsetDecoration(requireContext()))
        (binding.imagesListRecyclerView.layoutManager as GridLayoutManager)
            .spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if ((position + 1) % 3 == 0) 2 else 1
                }
            }
    }

    private fun showImage(url: String, itemView: View) {
        binding.imageContainer.imageView.load(url)
        binding.imageContainer.container.isVisible = true
        binding.imageContainer.backgroundView.setOnClickListener {
            binding.imageContainer.container.visibility = View.GONE
        }
    }

    private fun initToolbar() {
        binding.toolbarInclude.toolbarWithBack.updateLayoutParamsByWindowInsetsTop()
        binding.toolbarInclude.toolbarWithBack.setTitle(R.string.movie_info_images_block_title)
        binding.toolbarInclude.toolbarWithBack.isTitleCentered = true
        binding.toolbarInclude.toolbarWithBack.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.images.collectLatest {
                imagesAdapter.submitData(it)
            }
        }
    }
}