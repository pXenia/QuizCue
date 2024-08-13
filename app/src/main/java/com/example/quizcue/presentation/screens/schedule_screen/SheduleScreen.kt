package com.example.quizcue.presentation.screens.schedule_screen

import androidx.compose.runtime.Composable
import java.util.Date

@Composable
fun ScheduleScree(){
    val events = listOf(
        Event(Date(2024, 0, 15), "New Year Celebration"),
        Event(Date(2024, 1, 21), "Team Building Workshop"),
        Event(Date(2024, 2, 14), "Valentine's Day"),
        Event(Date(2024, 3, 17), "St. Patrick's Day"),
        Event(Date(2024, 4, 1), "April Fools' Day"),
        Event(Date(2024, 5, 5), "Cinco de Mayo"),
        Event(Date(2024, 6, 21), "Summer Solstice"),
        Event(Date(2024, 7, 1), "Independence Day"),
        Event(Date(2025, 7, 15), "Company Annual Meeting"),
        Event(Date(2024, 12, 25), "Christmas Day")
    )
    CalendarWithEvents(events)
}
