package com.example.quizcue.presentation

import androidx.compose.runtime.mutableStateOf

class LoginViewModel {
    var uiState = mutableStateOf(LoginUiState())
        private set
}