package com.al4apps.skillcinema.presentation.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.al4apps.skillcinema.domain.usecase.GetFilmsListByIdsUseCase
import com.al4apps.skillcinema.domain.usecase.GetPersonInfoUseCase
import com.al4apps.skillcinema.domain.usecase.SavePersonInDbUseCase
import javax.inject.Inject

class PersonViewModelFactory @Inject constructor(
    private val getPersonInfoUseCase: GetPersonInfoUseCase,
    private val getFilmsListByIdsUseCase: GetFilmsListByIdsUseCase,
    private val savePersonInDbUseCase: SavePersonInDbUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
            return PersonViewModel(
                getPersonInfoUseCase,
                getFilmsListByIdsUseCase,
                savePersonInDbUseCase
            ) as T
        }
        throw IllegalArgumentException("ViewModel not found")
    }
}