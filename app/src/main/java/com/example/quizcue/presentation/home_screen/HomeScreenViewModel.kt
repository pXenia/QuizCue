package com.example.quizcue.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.usecases.registration_UC.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    fun logout() = viewModelScope.launch {
        logoutUseCase.invoke()
    }
}