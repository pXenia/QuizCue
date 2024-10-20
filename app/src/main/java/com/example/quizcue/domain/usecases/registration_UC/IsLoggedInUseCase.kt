package com.example.quizcue.domain.usecases.registration_UC

import com.example.quizcue.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke() = flow {
        emit(authenticationRepository.isLoggerIn())
    }
}