package com.al4apps.skillcinema

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    val Context.firstStartDataStore: DataStore<Preferences> by preferencesDataStore(name = "d ")

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}