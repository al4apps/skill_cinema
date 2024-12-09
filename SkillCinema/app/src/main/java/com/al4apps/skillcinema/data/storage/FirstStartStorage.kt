package com.al4apps.skillcinema.data.storage

import kotlinx.coroutines.flow.Flow

interface FirstStartStorage {

    suspend fun setFlag()

    fun getFlag(): Flow<Boolean>
}