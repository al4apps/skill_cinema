package com.al4apps.skillcinema.presentation.movie

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import coil.load
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.databinding.PosterBlockViewBinding
import com.al4apps.skillcinema.domain.model.MovieDetailModel

class PosterBlockView(
    private val context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    private var binding: PosterBlockViewBinding
    var likeButton: ImageView
        private set
    var addToWatchButton: ImageView
        private set
    var watchedButton: ImageView
        private set
    var shareButton: ImageView
        private set
    var otherCommandsButton: ImageView
        private set

    init {
        val inflatedView = inflate(context, R.layout.poster_block_view, this)
        binding = PosterBlockViewBinding.bind(inflatedView)
        binding.posterImageView.setOnClickListener {
            binding.infoContainer.isVisible = !binding.infoContainer.isVisible
        }
        likeButton = binding.heartImageView
        addToWatchButton = binding.bookmarkImageView
        watchedButton = binding.eyeImageView
        shareButton = binding.shareImageView
        otherCommandsButton = binding.othersImageView
    }

    fun loadInfo(movie: MovieDetailModel) {
        binding.posterImageView.load(movie.posterUrl)
        val countryLengthAge = resources.getString(
            R.string.movie_info_country_length_age,
            movie.countries.first().country,
            minutesToHours(movie.filmLength),
            ageLimitToRuFormat(movie.ratingAgeLimits)
        )
        val genres = if (movie.genres.size > 1) {
            movie.genres[0].genre + ", " + movie.genres[1].genre
        } else movie.genres.first().genre
        val yearGenres = resources.getString(
            R.string.movie_info_year_genres,
            movie.year,
            genres
        )
        val ratingName = if (movie.ratingKinopoisk != null) {
            resources.getString(
                R.string.movie_info_rating_name,
                movie.ratingKinopoisk.toString(),
                movie.nameRu ?: movie.nameOriginal
            )
        } else movie.nameRu ?: movie.nameOriginal
        binding.countryLengthAgeTextView.text = countryLengthAge
        binding.yearGenresTextView.text = yearGenres
        binding.ratingNameTextView.text = ratingName
        movie.logoUrl?.let { binding.filmLogoImageView.load(it) }
    }

    fun updateMovieMutableInfo(mutableInfo: MovieMutableInfo) {
        likeButton.setImageResource(
            if (mutableInfo.isLiked) R.drawable.heart_red
            else R.drawable.heart
        )
        addToWatchButton.setImageResource(
            if (mutableInfo.isToWatch) R.drawable.bookmark_blue
            else R.drawable.bookmark
        )
        watchedButton.setImageResource(
            if (mutableInfo.isWatched) R.drawable.eye_blue
            else R.drawable.eye_closed
        )
    }

    private fun minutesToHours(minutes: Int): String {
        val hours = minutes / 60
        val min = minutes % 60
        return resources.getString(R.string.movie_info_hours_minutes, hours, min)
    }

    private fun ageLimitToRuFormat(age: String?): String {
        age ?: return ""
        return age.filter { it.isDigit() } + "+"
    }
}