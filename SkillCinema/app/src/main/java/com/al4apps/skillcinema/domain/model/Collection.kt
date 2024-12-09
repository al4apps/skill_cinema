package com.al4apps.skillcinema.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.al4apps.skillcinema.R
import com.al4apps.skillcinema.domain.Constants

sealed class CollectionListModel

data class Collection(
    val id: Int,
    val name: String = "",
    val isUsersCollection: Boolean,
    @DrawableRes
    val drawableRes: Int = when (id) {
        Constants.MOVIES_COLLECTION_LIKES_ID -> R.drawable.icon_likes
        Constants.MOVIES_COLLECTION_TO_WATCH_ID -> R.drawable.icon_to_watch
        else -> R.drawable.icon_user
    },
    var count: Int = 0
)

data class CollectionToMovie(
    val collection: Collection,
    val movieId: Int,
    var isMovieInCollection: Boolean
) : CollectionListModel()

data class CollectionHeader(
    @StringRes
    val nameRes: Int = R.string.bottom_dialog_add_to_collection_title,
    @DrawableRes
    val iconRes: Int = R.drawable.folder_plus
) : CollectionListModel()

data class CollectionFooter(
    @StringRes
    val nameRes: Int = R.string.bottom_dialog_add_new_collection_text,
    @DrawableRes
    val iconRes: Int = R.drawable.icon_plus
) : CollectionListModel()