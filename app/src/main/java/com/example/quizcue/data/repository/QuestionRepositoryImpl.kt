package com.example.quizcue.data.repository

import android.util.Log
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.repository.QuestionRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class QuestionRepositoryImpl(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : QuestionRepository {

    private val databaseRef = database.reference
        .child(auth.currentUser?.uid.toString())

    override fun upsertQuestion(question: Question) {
        val questionId = if (question.id == "") databaseRef.push().key ?: return else question.id
        val questionMap = hashMapOf<String, Any>(
            "id" to questionId,
            "text" to question.text,
            "hint" to question.hint,
            "answer" to question.answer,
            "course" to question.course,
            "isStudied" to question.isStudied,
            "isFavourite" to question.isFavourite,
        )
        databaseRef.child("questions").child(questionId)
            .setValue(questionMap)

    }

    override fun addFavourite(questionId: String, isFavourite: Boolean) {
        databaseRef.child("questions").child(questionId).child("isFavourite")
            .setValue(isFavourite)
    }

    override fun deleteQuestion(question: Question) {
        databaseRef.child("questions").child(question.id)
            .removeValue()
    }

    override fun getQuestions(): Flow<List<Question>> = callbackFlow {
        val questionsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = mutableListOf<Question>()
                snapshot.child("questions").children.forEach { child ->
                    val id = child.child("id").getValue(String::class.java) ?: ""
                    val text = child.child("text").getValue(String::class.java) ?: ""
                    val hint = child.child("hint").getValue(String::class.java) ?: ""
                    val answer = child.child("answer").getValue(String::class.java) ?: ""
                    val course = child.child("course").getValue(String::class.java) ?: ""
                    val isStudied = child.child("isStudied").getValue(Boolean::class.java) ?: false
                    val isFavourite = child.child("isFavourite").getValue(Boolean::class.java) ?: false
                    val createdDate = child.child("createdDate").getValue(Timestamp::class.java) ?: Timestamp.now()

                    val question = Question(
                        id, text, hint, answer, course, isStudied, isFavourite, createdDate
                    )
                    questions.add(question)
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

    override fun getQuestionById(questionId: String, onSuccess: (Question?) -> Unit) {
        databaseRef.child("questions").child(questionId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val question = snapshot.getValue(Question::class.java)
                    onSuccess(question)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}