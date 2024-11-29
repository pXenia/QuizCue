package com.example.quizcue.presentation.competition_screen

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.quizcue.domain.model.User
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.repository.CompetitionRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CompetitionViewModel @Inject constructor(
    private val competitionRepository: CompetitionRepository,
    private val authenticationRepository: AuthenticationRepository,
    savedStateHandle: SavedStateHandle,
    ): ViewModel() {

    private val _uiState = MutableStateFlow(uiState())
    val uiState: StateFlow<uiState> = _uiState

    var addOpponentResult: String? = null

    init {
        val competitionId = savedStateHandle["competitionId"] ?: ""
        if (competitionId != "") {
            competitionRepository.getCompetitionById(competitionId) { competition ->
                if (competition != null) {
                    authenticationRepository.getUserInfo(competition.user1){ user ->
                        user?.let {
                            _uiState.update { uiState ->
                                uiState.copy(
                                    user1 = it
                                )
                            }
                        }
                    }

                    authenticationRepository.getUserInfo(competition.user2){ user ->
                        user?.let {
                            _uiState.update { uiState ->
                                uiState.copy(
                                    user2 = it
                                )
                            }
                        }
                    }

                    _uiState.update { uiState ->
                        uiState.copy(
                            competitionId = competitionId,
                            date = competition.challengeDate,
                            prize = competition.prize,
                            user1TestScore = competition.user1TestScore,
                            user1CardScore = competition.user1CardScore,
                            user2TestScore = competition.user2TestScore,
                            user2CardScore = competition.user2CardScore
                        )
                    }

                }
            }
        }
    }

    fun addCompetition() {
        competitionRepository.addCompetition(
            prize = uiState.value.prize,
            challengeDate = uiState.value.date,
        ) {
            _uiState.update { uiState ->
                uiState.copy(
                    competitionId = it
                )
            }
        }
    }

    fun addOpponent() {
        competitionRepository.addOpponent(
            competitionId = uiState.value.competitionId,
        ) {
            addOpponentResult = it
        }
    }


}

data class uiState(
    val competitionId: String = "",
    val date: Long = 0L,
    val prize: String = "",
    val user1: User? = null,
    val user2: User? = null,
    val user1TestScore: Int = 0,
    val user1CardScore: Int = 0,
    val user2TestScore: Int = 0,
    val user2CardScore: Int = 0
)