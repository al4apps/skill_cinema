package com.al4apps.skillcinema.data.settings

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferenceKeys {
    val FIRST_START_FLAG_KEY = booleanPreferencesKey("first_start_flag")

    const val FIRST_START_DATA_STORE_NAME = "first_start_data_store"
}