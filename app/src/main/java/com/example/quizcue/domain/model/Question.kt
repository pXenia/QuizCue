package com.example.quizcue.domain.model

import com.google.firebase.Timestamp
import java.util.UUID

data class Question(
    val id: String = "",
    val text: String = "",
    val hint: String = "",
    val answer: String = "",
    val module: Int = -1,
    val isStudied: Boolean = false,
    val numberOfRepetitions: Int = 0,
    val createdDate: Timestamp = Timestamp.now()
)