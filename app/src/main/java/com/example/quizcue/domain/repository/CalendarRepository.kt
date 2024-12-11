package com.example.quizcue.domain.repository

import com.example.quizcue.domain.model.CalendarData
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    fun getData(): Flow<List<CalendarData>>
    fun updateQuizStat(date: Long, quizScore: Int)
    fun updateRepetitionNumber(date: Long)
}