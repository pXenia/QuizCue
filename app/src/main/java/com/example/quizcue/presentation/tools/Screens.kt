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
import com.example.quizcue.R

sealed class Screen(
    val route: String,
    val icon: ImageVector
){
    object Home: Screen("home", Icons.Filled.Home)
    object Courses: Screen("course", Icons.Default.School)
    object Schedule: Screen("schedule", Icons.Default.Schedule)
    object Questions: Screen("questions", Icons.Default.Schedule)
}