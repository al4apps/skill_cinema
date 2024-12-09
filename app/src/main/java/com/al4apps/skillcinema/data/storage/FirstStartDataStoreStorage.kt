package com.al4apps.skillcinema.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.al4apps.skillcinema.data.settings.PreferenceKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FirstStartDataStoreStorage @Inject constructor(
    private val firstStartDataStore: DataStore<Preferences>,
) : FirstStartStorage {
    override suspend fun setFlag() {
        firstStartDataStore.edit { preferences ->
            preferences[PreferenceKeys.FIRST_START_FLAG_KEY] = false
        }
    }

    override fun getFlag(): Flow<Boolean> {
        return firstStartDataStore.data.map {
            it[PreferenceKeys.FIRST_START_FLAG_KEY] ?: true
        }
    }
}