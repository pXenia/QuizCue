package com.example.quizcue.domain.repository

import com.example.quizcue.domain.Response
import com.example.quizcue.domain.model.Competition
import kotlinx.coroutines.flow.Flow

interface CompetitionRepository {
    suspend fun addOpponent(opponentId: String, challengeDate: Long)
    suspend fun getCompetitionById(competitionId: String, onSuccess: (Competition?) -> Unit)
}