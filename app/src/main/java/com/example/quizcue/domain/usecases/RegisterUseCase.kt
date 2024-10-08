package com.example.quizcue.domain.usecases

import com.example.quizcue.domain.repository.AuthenticationRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authenticationRepository.register(email, password)
}