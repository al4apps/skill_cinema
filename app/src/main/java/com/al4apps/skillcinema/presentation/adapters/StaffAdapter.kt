package com.al4apps.skillcinema.presentation.adapters

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemStaffBinding
import com.al4apps.skillcinema.domain.model.StaffModel
import com.al4apps.skillcinema.utils.inflate

class StaffAdapter(
    private val isMatchParent: Boolean,
    private val onItemClick: (id: Int, itemView: View) -> Unit
) : ListAdapter<StaffModel, StaffViewHolder>(StaffDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        return StaffViewHolder(
            parent.inflate(R.layout.item_staff, false),
            onItemClick,
            isMatchParent
        )
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class StaffDiffUtil : DiffUtil.ItemCallback<StaffModel>() {
        override fun areItemsTheSame(oldItem: StaffModel, newItem: StaffModel): Boolean {
            return oldItem.staffId == newItem.staffId
        }

        override fun areContentsTheSame(oldItem: StaffModel, newItem: StaffModel): Boolean {
            return oldItem == newItem
        }

    }
}

class StaffViewHolder(
    view: View,
    onItemClick: (id: Int, itemView: View) -> Unit,
    private val isMatchParent: Boolean
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemStaffBinding.bind(view)
    private var staffId: Int? = null

    init {
        view.setOnClickListener { staffId?.let { onItemClick(it, itemView) } }
    }

    fun bind(staff: StaffModel) {
        itemView.transitionName = itemView.resources.getString(
            R.string.item_transition_name,
            staff.staffId
        )
        if (isMatchParent) binding.root.layoutParams.width = LayoutParams.MATCH_PARENT
        staffId = staff.staffId
        binding.staffNameTextView.text = staff.nameRu
        binding.staffDescriptionTextView.text = staff.description ?: staff.professionText
        binding.photoImageView.load(staff.posterUrl)
    }
}