package com.example.quizcue.presentation.courses_screen

import com.example.quizcue.presentation.edit_question_screen.EditQuestionEvent

sealed class EditCourseEvent {
    data class EnteredTextCourse (val value: String): EditCourseEvent()
    data class EnteredDescriptionCourse (val value: String): EditCourseEvent()
    object SaveCourse: EditCourseEvent()
}
