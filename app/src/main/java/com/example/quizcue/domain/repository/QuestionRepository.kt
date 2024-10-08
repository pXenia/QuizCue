package com.example.quizcue.domain.repository

import com.example.quizcue.domain.Response
import com.example.quizcue.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun addQuestion(question: Question):  Response<Void?>
    fun getQuestions(): Flow<Response<List<Question>>>
}