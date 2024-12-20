package com.example.quizcue.presentation.competitionscreen

sealed class AddCompetitionEvent {
    data class EditCompetitionDate(val value: Long): AddCompetitionEvent()
    data class EditCompetitionPrize(val value: String): AddCompetitionEvent()
}