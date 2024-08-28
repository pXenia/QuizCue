package com.example.quizcue.domain.usecases

import com.example.quizcue.domain.AuthenticationRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke() = authenticationRepository.logout()
}