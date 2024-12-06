package com.example.quizcue.domain.model

data class Quiz(
    val id: String = "",
    val date: Long,
    val course: String,
    val score: Int
)
