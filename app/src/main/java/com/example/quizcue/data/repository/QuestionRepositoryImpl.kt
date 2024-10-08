package com.example.quizcue.data.repository

import com.example.quizcue.domain.Response
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.repository.QuestionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class QuestionRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : QuestionRepository {

    private val questionsCollection = firestore.collection("questions")

    override suspend fun addQuestion(question: Question): Response<Void?> {
        return  try {
            val userId = auth.currentUser?.uid ?: throw Exception ("User not logged in")
            val document = questionsCollection.document(userId).collection("userQuestions").document()
            document.set(question).await()
            Response.Success(null)
        } catch (e: Exception) {
            Response.Error(e.toString())
        }
    }

    override fun getQuestions(): Flow<Response<List<Question>>> = flow {
        try {
            val userId = auth.currentUser?.uid ?: throw Exception ("User not logged in")
            val snapshot = questionsCollection.document(userId).collection("userQuestions").get().await()
            val questions = snapshot.toObjects(Question::class.java)
            emit(Response.Success(questions))
        } catch (e: Exception) {
            emit(Response.Error(e.toString()))
        }
    }
}