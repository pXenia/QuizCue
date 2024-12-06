package com.example.quizcue.presentation.editquestionscreen

sealed class EditQuestionEvent {
    data class EnteredTextQuestion (val value: String): EditQuestionEvent()
    data class EnteredHintQuestion (val value: String): EditQuestionEvent()
    data class EnteredAnswerQuestion (val value: String): EditQuestionEvent()
    object SaveQuestion: EditQuestionEvent()
}