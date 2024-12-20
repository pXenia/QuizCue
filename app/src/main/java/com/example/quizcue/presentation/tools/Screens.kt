package com.example.quizcue.presentation.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val icon: ImageVector?
){
    object Home: Screen("home", Icons.Filled.Home)
    object Courses: Screen("course", Icons.Default.School)
    object Schedule: Screen("schedule", Icons.Default.Schedule)
    object Questions: Screen("questions", null)
    object LearnCard: Screen("learn_card", null)
    object Login: Screen("login", null)
    object Register: Screen("register", null)
    object EditQuestion: Screen("edit_question", null)
    object AddCourse: Screen("add_course", null)
    object Quiz: Screen("quiz", null)
    object Competition: Screen("competition", null)
    object ResultQuiz: Screen("result_quiz", null)
    object AddCompetitionDialog: Screen("add_competition", null)
    object QuizNavGraph: Screen("quiz_nav_graph", null)
    object CompetitionNavGraph: Screen("competition_nav_graph", null)
}