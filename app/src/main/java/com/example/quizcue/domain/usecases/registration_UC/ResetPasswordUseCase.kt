package com.example.quizcue.domain.usecases.registration_UC

import com.example.quizcue.domain.repository.AuthenticationRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String) = authenticationRepository.resetPassword(email)
}