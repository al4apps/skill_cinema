package com.al4apps.skillcinema.presentation.start

object SplashScreenObject {

    var isReady = false
        private set

    fun updateReadyStatus() {
        isReady = true
    }
}