package com.al4apps.skillcinema.presentation.movie

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.StaffBlockViewBinding
import com.al4apps.skillcinema.domain.model.StaffInfoModel
import com.al4apps.skillcinema.domain.model.StaffType
import com.al4apps.skillcinema.presentation.adapters.StaffAdapter

class StaffBlockView constructor(
    context: Context,
    attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {

    private var binding: StaffBlockViewBinding
    var countTextView: TextView
    private lateinit var staffAdapter: StaffAdapter

    init {
        val inflatedView = inflate(context, R.layout.staff_block_view, this)
        binding = StaffBlockViewBinding.bind(inflatedView)
        countTextView = binding.titleBlock.countTextView
    }

    fun setAdapter(adapter: StaffAdapter) {
        staffAdapter = adapter
        binding.staffRecyclerView.adapter = adapter
    }

    fun initBlock(staffModel: StaffInfoModel) {
        binding.titleBlock.titleTextView.setText(staffModel.title)
        if (staffModel.count > staffModel.staffList.size) {
            binding.titleBlock.countTextView.text =
                resources.getString(R.string.movie_info_item_count, staffModel.count)
        }
        binding.staffRecyclerView.layoutManager = if (staffModel.type == StaffType.ACTOR) {
            GridLayoutManager(context, 4, GridLayoutManager.HORIZONTAL, false)
        } else {
            GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        }
        staffAdapter.submitList(staffModel.staffList)
    }
}