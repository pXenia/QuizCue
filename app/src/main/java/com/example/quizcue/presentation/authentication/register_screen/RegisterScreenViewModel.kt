package com.example.quizcue.presentation.authentication.register_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.Response
import com.example.quizcue.domain.usecases.registration_UC.RegisterUseCase
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private var _registerFlow = MutableSharedFlow<Response<AuthResult>>()
    val registerFlow = _registerFlow

    fun register(email: String, password: String, name: String, imageUri: Uri?) = viewModelScope.launch {
        registerUseCase.invoke(email, password, name, imageUri).collect {
            _registerFlow.emit(it)
        }
    }
}