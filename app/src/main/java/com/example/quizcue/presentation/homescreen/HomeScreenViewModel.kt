package com.example.quizcue.presentation.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.model.User
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.usecases.registration_UC.LogoutUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    authenticationRepository: AuthenticationRepository,
    auth: FirebaseAuth,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(User())
    val uiState: StateFlow<User> = _uiState
    val email = auth.currentUser?.email ?: ""

    init {
        authenticationRepository.getUserInfo(auth.currentUser?.uid ?: ""){user ->
            user?.let {
                _uiState.update {
                    it.copy(
                        name = user.name,
                        photo = user.photo,
                        competitionId = user.competitionId
                    )
                }
            }
        }
    }

    fun logout() = viewModelScope.launch {
        logoutUseCase.invoke()
    }
}

