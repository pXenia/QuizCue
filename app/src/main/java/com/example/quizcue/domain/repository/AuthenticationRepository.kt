package com.example.quizcue.domain.repository

import android.net.Uri
import com.example.quizcue.domain.Response
import com.example.quizcue.domain.model.User
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun login(email: String, password: String): Flow<Response<AuthResult>>
    suspend fun register(email: String, password: String, name: String, imageUri: Uri?): Flow<Response<AuthResult>>
    suspend fun resetPassword(email: String): Flow<Response<Void?>>
    suspend fun logout()
    suspend fun userUid(): String
    suspend fun isLoggerIn(): Boolean
    fun getUserInfo(userId: String, onSuccess: (User?) -> Unit)
}