package com.example.quizcue.domain.repository

import com.example.quizcue.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun upsertQuestion(question: Question, onSuccess: () -> Unit)
    suspend fun deleteQuestion(question: Question, onSuccess: () -> Unit)
    suspend fun getQuestionById(questionId: String, onSuccess: (Question?) -> Unit)
    fun getQuestions(): Flow<List<Question>>
}