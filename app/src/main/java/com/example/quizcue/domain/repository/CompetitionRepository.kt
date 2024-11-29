package com.example.quizcue.domain.repository

import com.example.quizcue.domain.Response
import com.example.quizcue.domain.model.Competition
import com.example.quizcue.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CompetitionRepository {
    suspend fun addCompetition(prize: String, challengeDate: Long, onSuccess: (String) -> Unit)
    fun addOpponent(competitionId: String, onSuccess: (String) -> Unit)
    fun getCompetitionById(competitionId: String, onSuccess: (Competition?) -> Unit)
}