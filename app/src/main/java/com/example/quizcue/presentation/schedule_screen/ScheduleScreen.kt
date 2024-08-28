package com.example.quizcue.presentation.schedule_screen

import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

@Composable
fun ScheduleScree(){
    val random = Random()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val questions = List(90) {
        val month = 7
        val day = random.nextInt(15) + 1
        val calendar = Calendar.getInstance()
        calendar.set(2024, month, day)

        QuestionTitle(
            date = calendar.time,
            description = "Question ${it + 1}: Random question text here."
        )
    }
    CalendarWithEvents(questions)
}
