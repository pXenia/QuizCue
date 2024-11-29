package com.example.quizcue.data.repository

import android.util.Log
import com.example.quizcue.domain.Response
import com.example.quizcue.domain.model.Competition
import com.example.quizcue.domain.model.Course
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.repository.CompetitionRepository
import com.google.android.play.integrity.internal.al
import com.google.android.play.integrity.internal.c
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

    override fun addOpponent(
        competitionId: String,
        onSuccess: (String) -> Unit
    ){
        val currentUser = auth.currentUser?.uid.toString()
        var user2: String? = null
        databaseRef.child("competitions").child(competitionId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user2 = snapshot.child("user2").getValue(String::class.java)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Failed to retrieve question by id", error.toException())
                }
            })
        if (user2 == ""){
            databaseRef.child("competitions").child(competitionId).child("user2").setValue(currentUser)
            onSuccess("Вы успешно присоединились к соревнованию!")
            databaseRef.child("users").child(currentUser).child("competitionId").setValue(competitionId)
        } else {
            onSuccess("К сожалению, в сореановании не может быть более 2х участников")
        }
    }

    override suspend fun addCompetition(prize: String, challengeDate: Long, onSuccess: (String) -> Unit){
        val currentUser = auth.currentUser?.uid.toString()
        var competitionId: String? = null
        databaseRef.child(currentUser)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    competitionId = snapshot.child("competitionId").getValue(String::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Failed to retrieve question by id", error.toException())
                }
            })

        if (competitionId == null) {
            competitionId = databaseRef.child("competitions").push().key
            val competitionMap = hashMapOf<String, Any?>(
                "id" to competitionId,
                "user1" to currentUser,
                "user2" to "",
                "prize" to prize,
                "challengeDate" to challengeDate,
            )
            competitionId?.let {
                databaseRef.child("competitions").child(it).setValue(competitionMap)
                onSuccess(it)
                databaseRef.child("users").child(currentUser).child("competitionId").setValue(competitionId)
            }
        } else {
            onSuccess("Вы уже учавствуете в соревновании! Сначала завершите его.")
        }
    }

    override fun getCompetitionById(competitionId: String, onSuccess: (Competition?) -> Unit) {
        databaseRef.child("competitions").child(competitionId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { child ->
                        val id = child.child("id").getValue(String::class.java) ?: ""
                        val user1 = child.child("user1").getValue(String::class.java) ?: ""
                        val user2 = child.child("user2").getValue(String::class.java) ?: ""
                        val prize = child.child("prize").getValue(String::class.java) ?: ""
                        val challengeDate =
                            child.child("challengeDate").getValue(Long::class.java) ?: 0L
                        val competition = Competition(
                            id, user1, user2, prize, challengeDate, 0, 0, 0, 0
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