package com.example.quizcue.domain.repository

import com.example.quizcue.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    fun upsertQuestion(question: Question)
    fun deleteQuestion(question: Question)
    fun getQuestionById(questionId: String, onSuccess: (Question?) -> Unit)
    fun getQuestions(): Flow<List<Question>>
    fun addFavourite(questionId: String, isFavourite: Boolean)
}