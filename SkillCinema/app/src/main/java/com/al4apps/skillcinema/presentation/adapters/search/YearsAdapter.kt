package com.al4apps.skillcinema.presentation.adapters.search

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemYearBinding
import com.al4apps.skillcinema.presentation.searchsettings.years.Year
import com.al4apps.skillcinema.utils.inflate

class YearsAdapter(
    private val onItemClick: (item: Year) -> Unit
) : ListAdapter<Year, YearsViewHolder>(YearsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearsViewHolder {
        return YearsViewHolder(parent.inflate(R.layout.item_year, false), onItemClick)
    }

    override fun onBindViewHolder(holder: YearsViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class YearsDiffUtil : DiffUtil.ItemCallback<Year>() {
        override fun areItemsTheSame(oldItem: Year, newItem: Year): Boolean {
            return oldItem.year == newItem.year
        }

        override fun areContentsTheSame(oldItem: Year, newItem: Year): Boolean {
            return oldItem == newItem
        }

    }
}

class YearsViewHolder(
    view: View,
    onItemClick: (item: Year) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemYearBinding.bind(view)
    private var item: Year? = null
    init {
        view.setOnClickListener {
            item?.let { year ->
                year.isSelected = !year.isSelected
                updateItem(year)
                onItemClick(year)
            }
        }
    }

    fun bind(item: Year) {
        this.item = item
        updateItem(item)
        binding.parameterNameTextView.text = item.year.toString()
        binding.parameterNameTextView2.text = item.year.toString()
    }
    private fun updateItem(item: Year) {
        binding.selectedContainer.isVisible = item.isSelected
        binding.parameterNameTextView.isVisible = !item.isSelected
    }
}
