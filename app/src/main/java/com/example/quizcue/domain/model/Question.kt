package com.example.quizcue.domain.model

import com.google.firebase.Timestamp

data class Question(
    val id: String = "",
    val text: String = "",
    val hint: String = "",
    val answer: String = "",
    val course: String = "",
    val isStudied: Boolean = false,
    val isFavourite: Boolean = false,
    val createdDate: Timestamp = Timestamp.now()
)