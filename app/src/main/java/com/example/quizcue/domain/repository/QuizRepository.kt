package com.example.quizcue.domain.repository

import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.model.Quiz

interface QuizRepository {
   fun addQuiz(quiz: Quiz)
}