package com.example.quizcue.domain.usecases

import com.example.quizcue.domain.AuthenticationRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authenticationRepository.login(email, password)
}