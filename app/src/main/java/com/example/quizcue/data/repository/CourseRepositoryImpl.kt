package com.example.quizcue.data.repository

import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.repository.QuestionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow

class CourseRepositoryImpl(
    private val database: FirebaseDatabase,
private val auth: FirebaseAuth
) : QuestionRepository {
    override suspend fun upsertQuestion(question: Question, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteQuestion(question: Question, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getQuestionById(questionId: String, onSuccess: (Question?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getQuestions(): Flow<List<Question>> {
        TODO("Not yet implemented")
    }

}