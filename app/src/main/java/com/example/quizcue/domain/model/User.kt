package com.example.quizcue.domain.model

import coil3.Bitmap

data class User (
    val name: String = "",
    val photo: Bitmap? = null,
    val competitionId: String = ""
)