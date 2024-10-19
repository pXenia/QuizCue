package com.example.quizcue.data.repository

import android.util.Log
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.repository.QuestionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class QuestionRepositoryImpl(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : QuestionRepository {

    private val databaseRef = database.reference
        .child(auth.currentUser?.uid.toString())

    override suspend fun upsertQuestion(question: Question, onSuccess: () -> Unit) {
        val questionId = if (question.id == "") databaseRef.push().key ?: return else question.id
        val questionMap = hashMapOf<String, Any>(
            "text" to question.text,
            "hint" to question.hint,
            "answer" to question.answer,
        )
        databaseRef.child(questionId)
            .setValue(questionMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                Log.e("FirebaseError", "Failed to add question", exception)
            }
    }

    override suspend fun deleteQuestion(question: Question, onSuccess: () -> Unit) {
        databaseRef.child(question.id)
            .removeValue()
            .addOnSuccessListener { onSuccess() }
    }

    override fun getQuestions(): Flow<List<Question>> = callbackFlow {
        val questionsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = mutableListOf<Question>()
                snapshot.children.forEach { child ->
                    val question = child.getValue(Question::class.java)
                    question?.let { questions.add(it) }
                }
                trySend(questions).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Failed to retrieve questions", error.toException())
            }
        }

        databaseRef.addValueEventListener(questionsListener)

        awaitClose { databaseRef.removeEventListener(questionsListener) }
    }

}