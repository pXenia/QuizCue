package com.example.quizcue.data.repository

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation.findNavController
import com.example.quizcue.MainActivity
import com.example.quizcue.R
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.Response
import com.example.quizcue.domain.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
            emit(Response.Error(e.localizedMessage ?:  context.getString(R.string.error)))
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String,
        imageUri: Uri?
    ): Flow<Response<AuthResult>> = flow {
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
            emit(Response.Error(e.localizedMessage ?:  context.getString(R.string.error)))
        }
    }

    override suspend fun resetPassword(email: String): Flow<Response<Void?>> = flow {
        try {
            emit(Response.Loading)
            val data = auth.sendPasswordResetEmail(email).await()
            emit(Response.Success(data))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: context.getString(R.string.error)))
        }
    }

    override suspend fun logout() = auth.signOut()

    override suspend fun userUid(): String = auth.currentUser?.uid ?: ""

    override suspend fun isLoggerIn(): Boolean = auth.currentUser == null

    override fun getUserInfo(userId: String, onSuccess: (User?) -> Unit) {
        var name: String?
        var pictureToBitmap: Bitmap?
        var competitionId: String?
        database.reference.child("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    name = snapshot.child("name").getValue(String::class.java) ?: ""
                    val picture = snapshot.child("picture").getValue(String::class.java)
                    competitionId =
                        snapshot.child("competitionId").getValue(String::class.java) ?: ""
                    pictureToBitmap = picture?.let {
                        val bytes = Base64.decode(it, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    }
                    val user = User(
                        name = name ?: "",
                        photo = pictureToBitmap,
                        competitionId = competitionId ?: ""
                    )
                    onSuccess(user)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}