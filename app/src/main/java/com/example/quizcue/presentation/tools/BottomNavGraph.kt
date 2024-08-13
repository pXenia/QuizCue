package com.example.quizcue.presentation.tools

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizcue.presentation.screens.CoursesScreen
import com.example.quizcue.presentation.screens.HomeScreen
import com.example.quizcue.presentation.screens.schedule_screen.ScheduleScree

@Composable
fun BottomNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route){
            HomeScreen()
        }
        composable(route = Screen.Courses.route){
            CoursesScreen()
        }
        composable(route = Screen.Schedule.route){
            ScheduleScree()
        }

    }
}