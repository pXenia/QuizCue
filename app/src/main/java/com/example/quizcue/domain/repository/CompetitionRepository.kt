package com.example.quizcue.domain.repository

import com.example.quizcue.domain.Response
import com.example.quizcue.domain.model.Competition
import com.example.quizcue.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CompetitionRepository {
    fun addCompetition(prize: String, challengeDate: Long, onSuccess: (String) -> Unit)
    fun addOpponent(competitionId: String, onSuccess: (String) -> Unit)
    fun deleteCompetition(competitionId: String)
    fun getCompetitionById(competitionId: String, onSuccess: (Competition?) -> Unit)
}