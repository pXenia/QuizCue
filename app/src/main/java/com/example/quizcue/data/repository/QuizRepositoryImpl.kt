package com.example.quizcue.data.repository

import android.util.Log
import com.example.quizcue.domain.model.Quiz
import com.example.quizcue.domain.repository.QuizRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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
    
}