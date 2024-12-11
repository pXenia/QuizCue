package com.example.quizcue.data.repository

import android.util.Log
import com.example.quizcue.domain.model.CalendarData
import com.example.quizcue.domain.repository.CalendarRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CalendarRepositoryImpl (
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : CalendarRepository {
    private val databaseRef = database.reference
        .child(auth.currentUser?.uid.toString()).child("calendar")

    override fun getData(): Flow<List<CalendarData>> = callbackFlow {
        val calendarListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val questions = mutableListOf<CalendarData>()
                snapshot.children.forEach { child ->
                    val data = child.child("date").getValue(Long::class.java) ?: 0L
                    val quizScore = child.child("quizScore").getValue(Int::class.java) ?: 0
                    val repetitionNumber = child.child("repetitionNumber").getValue(Int::class.java) ?: 0
                    val calendarData = CalendarData(
                        data, quizScore, repetitionNumber
                    )
                    questions.add(calendarData)
                }
                trySend(questions).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        databaseRef.addValueEventListener(calendarListener)

        awaitClose { databaseRef.removeEventListener(calendarListener) }
    }

    override fun updateQuizStat(date: Long, quizScore: Int) {
        val calendarDataRef = databaseRef.child(date.toString())

        calendarDataRef.get().addOnSuccessListener { child ->
            val data = child.child("date").getValue(Long::class.java)
            val quizScoreOld = child.child("quizScore").getValue(Int::class.java) ?: 0
            if (data == null)
                calendarDataRef.child("data").setValue(date)
            calendarDataRef.child("quizScore").setValue(quizScoreOld + quizScore)
        }
    }

    override fun updateRepetitionNumber(date: Long) {
        val calendarDataRef = databaseRef.child(date.toString())

        calendarDataRef.get().addOnSuccessListener { child ->
            val data = child.child("date").getValue(Long::class.java) ?: 0L
            val repetitionNumberOld = child.child("repetitionNumber").getValue(Int::class.java) ?: 0
            if (data == 0L)
                calendarDataRef.child("date").setValue(date)
            calendarDataRef.child("repetitionNumber").setValue(repetitionNumberOld + 1)
        }
    }
}
