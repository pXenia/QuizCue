package com.example.quizcue.data.repository

import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.Response
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
): AuthenticationRepository {
    override suspend fun login(email: String, password: String): Flow<Response<AuthResult>> = flow {
        try {
            emit(Response.Loading)
            val data = auth.signInWithEmailAndPassword(email, password).await()
            emit(Response.Success(data))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Oops, something went wrong."))
        }
    }


    override suspend fun register(email: String, password: String, name: String): Flow<Response<AuthResult>> = flow {
        try {
            emit(Response.Loading)
            val data = auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val userId= FirebaseAuth.getInstance().currentUser?.uid
                    val user = hashMapOf<String, Any>(
                        "name" to name,
                        "picture" to "",
                    )
                    database.reference.child("users")
                        .child(userId!!)
                        .setValue(user)
                        .addOnSuccessListener {  }
                        .addOnFailureListener{}
                }.await()
            emit(Response.Success(data))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Oops, something went wrong."))
        }
    }

    override suspend fun resetPassword(email: String): Flow<Response<Void?>> = flow {
        try {
            emit(Response.Loading)
            val data = auth.sendPasswordResetEmail(email).await()
            emit(Response.Success(data))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Oops, something went wrong."))
        }
    }

    override suspend fun logout() = auth.signOut()

    override suspend fun userUid(): String = auth.currentUser?.uid ?: ""

    override suspend fun isLoggerIn(): Boolean = auth.currentUser == null
}