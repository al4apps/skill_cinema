package com.al4apps.skillcinema.presentation.adapters.collections

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.ItemPersonBinding
import com.al4apps.skillcinema.domain.model.MoviesAndPersonsDb
import com.al4apps.skillcinema.domain.model.PersonDbModel
import com.al4apps.skillcinema.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class PersonDbAdapterDelegate(
    private val onItemClick: (kinopoiskId: Int?, id: Int?, itemView: View) -> Unit
) : AbsListItemAdapterDelegate<PersonDbModel, MoviesAndPersonsDb, PersonDbViewHolder>() {
    override fun isForViewType(
        item: MoviesAndPersonsDb,
        items: MutableList<MoviesAndPersonsDb>,
        position: Int
    ): Boolean {
        return item is PersonDbModel
    }

    override fun onCreateViewHolder(parent: ViewGroup): PersonDbViewHolder {
        return PersonDbViewHolder(parent.inflate(R.layout.item_person, false), onItemClick)
    }

    override fun onBindViewHolder(
        item: PersonDbModel,
        holder: PersonDbViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}

class PersonDbViewHolder(
    view: View,
    private val onItemClick: (kinopoiskId: Int?, id: Int, itemView: View) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemPersonBinding.bind(view)
    private var id: Int? = null

    init {
        itemView.setOnClickListener {
            id?.let { onItemClick(null, it, itemView) }
        }
    }

    fun bind(person: PersonDbModel) {
        itemView.transitionName = itemView.resources.getString(
            R.string.item_transition_name,
            person.id
        )
        id = person.id
        binding.staffNameTextView.text = person.nameRu
        binding.staffDescriptionTextView.text = person.profession
        binding.photoImageView.load(person.posterUrl)
    }
}