package com.example.quizcue.presentation.authentication.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.Response
import com.example.quizcue.domain.usecases.registration_UC.LoginUseCase
import com.example.quizcue.domain.usecases.registration_UC.ResetPasswordUseCase
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _loginFlow = MutableSharedFlow<Response<AuthResult>>()
    val loginFlow = _loginFlow

    private val _resetPasswordFlow = MutableSharedFlow<Response<Void?>>()
    val resetPasswordFlow = _resetPasswordFlow

    fun login(email: String, password: String) = viewModelScope.launch {
        loginUseCase.invoke(email, password).collect{
            response ->
            _loginFlow.emit(response)
        }
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        resetPasswordUseCase.invoke(email).collect{
            response ->
            _resetPasswordFlow.emit(response)
        }
    }
}