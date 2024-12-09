package com.al4apps.skillcinema.data.repository

import com.al4apps.skillcinema.data.storage.FirstStartDataStoreStorage
import com.al4apps.skillcinema.data.storage.FirstStartStorage
import com.al4apps.skillcinema.domain.repository.StartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StartRepositoryImpl @Inject constructor(
    private val firstStartStorage: FirstStartDataStoreStorage,
) : StartRepository {
    override suspend fun setFirstStartFlag() {
        firstStartStorage.setFlag()
    }

    override fun getFirstStartFlag(): Flow<Boolean> {
        return firstStartStorage.getFlag()
    }
}