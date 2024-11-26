package com.example.quizcue.data.repository

import android.util.Log
import com.example.quizcue.domain.Response
import com.example.quizcue.domain.model.Competition
import com.example.quizcue.domain.model.Course
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.repository.CompetitionRepository
import com.google.android.play.integrity.internal.al
import com.google.android.play.integrity.internal.m
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow

class CompetitionRepositoryImpl(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
):CompetitionRepository {
    val databaseRef = database.reference

    override suspend fun addOpponent(
        opponentId: String,
        challengeDate: Long
    ){
        val currentUser = auth.currentUser?.uid.toString()
        val competitionId = databaseRef.push().key ?: return
        val competitionMap = hashMapOf<String, Any>(
            "id" to competitionId,
            "user1" to currentUser,
            "user2" to opponentId,
            "challengeDate" to challengeDate,
        )
        databaseRef.child(competitionId)
            .setValue(competitionId)
        val userCompetitionInfo = mapOf("competition" to competitionId)
        databaseRef.child("users").child(competitionId).setValue(userCompetitionInfo)
        databaseRef.child("users").child(opponentId).setValue(userCompetitionInfo)
    }
    override suspend fun getCompetitionById(competitionId: String, onSuccess: (Competition?) -> Unit) {
        databaseRef.child(competitionId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { child ->
                        val id = child.child("id").getValue(String::class.java) ?: ""
                        val user1 = child.child("user1").getValue(String::class.java) ?: ""
                        val user2 = child.child("user2").getValue(String::class.java) ?: ""
                        val challengeDate =
                            child.child("challengeDate").getValue(Long::class.java) ?: 0L
                        val competition = Competition(
                            id, user1, user2, challengeDate
                        )
                        onSuccess(competition)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Failed to retrieve competition by id", error.toException())
                }
            })
    }
}