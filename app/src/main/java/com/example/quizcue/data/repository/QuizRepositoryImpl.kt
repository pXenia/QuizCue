package com.example.quizcue.data.repository

import android.util.Log
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.model.Quiz
import com.example.quizcue.domain.repository.QuizRepository
import com.google.android.play.integrity.internal.c
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class QuizRepositoryImpl(
    private val database: FirebaseDatabase, 
    private val auth: FirebaseAuth
) : QuizRepository {

    private val databaseRef = database.reference
        .child(auth.currentUser?.uid.toString())

    override fun addQuiz(quiz: Quiz) {
        val quizId = if (quiz.id == "") databaseRef.push().key ?: return else quiz.id
        val quizMap = hashMapOf<String, Any>(
            "id" to quizId,
            "date" to quiz.date,
            "course" to quiz.course,
            "score" to quiz.score,
        )
        databaseRef.child("quiz").child(quizId)
            .setValue(quizMap)
            .addOnFailureListener { exception ->
                Log.e("FirebaseError", "Failed to add question", exception)
            }
    }

    override fun updateScore(score: Int, competitionId: String) {
        val currentUser = auth.currentUser?.uid ?: ""
        val competitionRef = databaseRef.child("competitions").child(competitionId)

        competitionRef.get().addOnSuccessListener { snapshot ->
            val user1 = snapshot.child("user1").getValue(String::class.java) ?: 0
            val user2 = snapshot.child("user2").getValue(String::class.java) ?: 0
            val user1TestScore = snapshot.child("user1TestScore").getValue(Int::class.java) ?: 0
            val user2TestScore = snapshot.child("user2TestScore").getValue(Int::class.java) ?: 0
            if (user1 == currentUser) {
                competitionRef.child("user1TestScore").setValue(user1TestScore + score)
            } else if (user2 == currentUser){
                competitionRef.child("user2TestScore").setValue(user2TestScore + score)
            }
        }.addOnFailureListener { exception ->
            Log.e("UpdateScore", "Ошибка при получении текущего счёта", exception)
        }
    }

}