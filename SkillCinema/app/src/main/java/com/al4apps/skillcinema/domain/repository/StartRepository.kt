package com.al4apps.skillcinema.domain.repository

import kotlinx.coroutines.flow.Flow

interface StartRepository {

    suspend fun setFirstStartFlag()

    fun getFirstStartFlag(): Flow<Boolean>
}