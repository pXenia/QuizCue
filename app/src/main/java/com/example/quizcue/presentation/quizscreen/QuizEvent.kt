package com.example.quizcue.presentation.quizscreen

sealed class QuizEvent {
    data class GenerateCorrectAnswer(val questionText: String) : QuizEvent()
    data class GenerateIncorrectAnswer(val questionText: String) : QuizEvent()
    object ClearAnswers : QuizEvent()
}