package com.example.quizcue.domain.model

data class Competition (
    val id: String,
    val user1: String,
    val user2: String,
    val prize: String,
    val challengeDate: Long,
    val user1TestScore: Int,
    val user2TestScore: Int,
)