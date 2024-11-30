package com.example.quizcue.presentation.competition_screen

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.model.Course
import com.example.quizcue.domain.model.User
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.repository.CompetitionRepository
import com.example.quizcue.presentation.courses_screen.EditCourseEvent
import com.example.quizcue.presentation.edit_question_screen.EditQuestionEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitionViewModel @Inject constructor(
    private val competitionRepository: CompetitionRepository,
    private val authenticationRepository: AuthenticationRepository,
    savedStateHandle: SavedStateHandle,
    ): ViewModel() {

    private val _uiState = MutableStateFlow(uiState())
    val uiState: StateFlow<uiState> = _uiState

    private var _competitionKey = mutableStateOf("")
    val competitionKey: State<String> = _competitionKey

    init {
        val competitionId = savedStateHandle["competitionId"] ?: ""
        if (competitionId != "") {
            getCompetitionById(competitionId)
        }
    }

    fun onEvent(event: AddCompetitionEvent) {
        when (event) {
            is AddCompetitionEvent.EditCompetitionPrize -> _uiState.update { it.copy(prize = event.value) }
            is AddCompetitionEvent.EditCompetitionDate -> _uiState.update { it.copy(date = event.value) }
        }
    }

    fun addCompetition() {
        viewModelScope.launch {
            competitionRepository.addCompetition(
                prize = uiState.value.prize,
                challengeDate = uiState.value.date,
            ) {
                _competitionKey.value = it
            }
        }
    }

    fun addOpponent(competitionId: String) {
        competitionRepository.addOpponent(
            competitionId = competitionId,
        ) {}
    }

    fun getCompetitionById(competitionId: String) {
        competitionRepository.getCompetitionById(competitionId) { competition ->
            if (competition != null) {
                authenticationRepository.getUserInfo(competition.user1) { user ->
                    user?.let {
                        _uiState.update { uiState ->
                            uiState.copy(
                                user1 = it
                            )
                        }
                    }
                }

                authenticationRepository.getUserInfo(competition.user2) { user ->
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

    fun deleteCompetition(competitionId: String){
        competitionRepository.deleteCompetition(competitionId)
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