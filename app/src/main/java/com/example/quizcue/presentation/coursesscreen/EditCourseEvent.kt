package com.example.quizcue.presentation.coursesscreen

sealed class EditCourseEvent {
    data class EnteredTextCourse (val value: String): EditCourseEvent()
    data class EnteredDescriptionCourse (val value: String): EditCourseEvent()
    object SaveCourse: EditCourseEvent()
}
