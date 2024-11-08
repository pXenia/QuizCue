package com.example.quizcue.presentation.home_screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.usecases.registration_UC.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {
    val userName: Flow<String> = authenticationRepository.getUserName()
    val userPhoto: Flow<Bitmap?> = authenticationRepository.getUserPhoto()
    val userEmail: Flow<String> = authenticationRepository.getUserEmail()

    fun logout() = viewModelScope.launch {
        logoutUseCase.invoke()
    }


}