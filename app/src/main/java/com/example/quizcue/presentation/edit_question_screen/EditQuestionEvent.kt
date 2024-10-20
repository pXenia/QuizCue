package com.example.quizcue.presentation.edit_question_screen

sealed class EditQuestionEvent {
    data class EnteredTextQuestion (val value: String): EditQuestionEvent()
    data class EnteredHintQuestion (val value: String): EditQuestionEvent()
    data class EnteredAnswerQuestion (val value: String): EditQuestionEvent()
    object SaveQuestion: EditQuestionEvent()
}