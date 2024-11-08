package com.example.quizcue.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.Response
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.InputStream
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth,
    private val context: Context
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


    override suspend fun register(email: String, password: String, name: String, imageUri: Uri?): Flow<Response<AuthResult>> = flow {
        try {
            emit(Response.Loading)

            val data = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = auth.currentUser?.uid ?: throw Exception("User ID not found")

            val imageBase64 = imageUri?.let { uri ->
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                bytes?.let { Base64.encodeToString(it, Base64.DEFAULT) }
            }

            val user = hashMapOf(
                "name" to name,
                "picture" to (imageBase64 ?: "")
            )
            database.reference.child("users").child(userId).setValue(user).await()

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

    override fun getUserEmail(): Flow<String> = flow {
        emit(auth.currentUser?.email ?: "")
    }

    override fun getUserName(): Flow<String> = flow {
        val userId = auth.currentUser?.uid ?: ""
        if (userId.isNotEmpty()) {
            try {
                val snapshot = database.reference.child("users").child(userId).child("name").get().await()
                emit(snapshot.getValue(String::class.java) ?: "")
            } catch (e: Exception) {
                emit("")
            }
        } else {
            emit("")
        }
    }

    override fun getUserPhoto(): Flow<Bitmap?> = flow {
        val userId = auth.currentUser?.uid ?: ""
        if (userId.isNotEmpty()) {
            try {
                val snapshot = database.reference.child("users").child(userId).child("picture").get().await()
                val base64Image = snapshot.getValue(String::class.java)

                val bitmap = base64Image?.let {
                    val bytes = Base64.decode(it, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }
                emit(bitmap)
            } catch (e: Exception) {
                emit(null)
            }
        } else {
            emit(null)
        }
    }

}