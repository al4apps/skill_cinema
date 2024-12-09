package com.al4apps.skillcinema.presentation.home

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.MoviesBlockViewBinding
import com.al4apps.skillcinema.domain.Constants.MOVIES_HORIZONTAL_BLOCK_SIZE
import com.al4apps.skillcinema.domain.model.MovieCollectionType
import com.al4apps.skillcinema.domain.model.MoviesAndPersonsCollection
import com.al4apps.skillcinema.domain.model.MoviesCollection
import com.al4apps.skillcinema.domain.model.SimilarModel
import com.al4apps.skillcinema.presentation.adapters.movies.DbItemsDelegationAdapter
import com.al4apps.skillcinema.presentation.adapters.movies.MoviesDelegationAdapter
import com.al4apps.skillcinema.presentation.decor.ItemOffsetDecoration

class MoviesBlockView constructor(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private val binding: MoviesBlockViewBinding
    private var moviesAdapter: MoviesDelegationAdapter? = null
    private var dbItemsAdapter: DbItemsDelegationAdapter? = null
    private val decor = ItemOffsetDecoration(context)
    var allTextView: TextView
        private set

    init {
        val inflatedView = inflate(context, R.layout.movies_block_view, this)
        binding = MoviesBlockViewBinding.bind(inflatedView)
        allTextView = binding.allTextTextView
    }

    fun setMoviesAdapter(adapter: MoviesDelegationAdapter) {
        moviesAdapter = adapter
        binding.moviesRecyclerView.adapter = moviesAdapter
        binding.moviesRecyclerView.addItemDecoration(decor)
    }

    fun setDbItemsAdapter(adapter: DbItemsDelegationAdapter) {
        dbItemsAdapter = adapter
        binding.moviesRecyclerView.adapter = dbItemsAdapter
        binding.moviesRecyclerView.addItemDecoration(decor)
    }

    fun setSimilarFilms(model: SimilarModel) {
        binding.titleTextView.setText(R.string.movie_info_similar_block_title)
        if (model.count > model.list.size) {
            binding.allTextTextView.isVisible = true
            val text = resources.getString(R.string.movie_info_item_count, model.count)
            binding.allTextTextView.text = text
        }
        moviesAdapter?.items = model.list
    }

    fun setMoviesCollection(moviesCollection: MoviesCollection) {
        setCountTextViewVisibility(moviesCollection.info.type)
        val title = if (moviesCollection.info.dynamicCollectionInfo != null) {
            val genre = resources.getString(moviesCollection.info.dynamicCollectionInfo.genreRes)
            val country =
                resources.getString(moviesCollection.info.dynamicCollectionInfo.countryRes)
            resources.getString(R.string.movie_type_dynamic_title, genre, country)
        } else {
            resources.getString(moviesCollection.info.title)
        }
        binding.titleTextView.text = title
        moviesAdapter?.items = moviesCollection.movies.take(MOVIES_HORIZONTAL_BLOCK_SIZE)
    }

    fun setMoviesAndPersonsCollection(collection: MoviesAndPersonsCollection) {
        binding.titleTextView.text = resources.getString(collection.collectionInfo.title)
        dbItemsAdapter?.items = collection.list.take(MOVIES_HORIZONTAL_BLOCK_SIZE)
    }

    private fun setCountTextViewVisibility(type: MovieCollectionType) {
        binding.allTextTextView.isVisible = !((type == MovieCollectionType.WITH_PERSON) ||
                (type == MovieCollectionType.FROM_DB_WATCHED) ||
                (type == MovieCollectionType.FROM_DB_VIEWS))
    }

    fun cleanInfo() {
        moviesAdapter?.items = emptyList()
        moviesAdapter = null
        binding.moviesRecyclerView.removeItemDecoration(decor)
        binding.moviesRecyclerView.adapter = null
    }

}