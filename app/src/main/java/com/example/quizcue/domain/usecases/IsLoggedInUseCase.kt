package com.example.quizcue.domain.usecases

import com.example.quizcue.domain.AuthenticationRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsLoggedInUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke() = flow {
        emit(authenticationRepository.isLoggerIn())
    }
}