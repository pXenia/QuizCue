package com.example.quizcue.presentation.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.quizcue.presentation.screens.CoursesScreen
import com.example.quizcue.presentation.screens.HomeScreen
import com.example.quizcue.presentation.screens.QuestionsScreen
import com.example.quizcue.presentation.screens.schedule_screen.ScheduleScree

@Composable
fun BottomNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route){
            HomeScreen(navController)
        }
        composable(route = Screen.Courses.route){
            CoursesScreen(navController)
        }
        composable(route = Screen.Schedule.route){
            ScheduleScree()
        }
        composable(route = Screen.Questions.route){
            QuestionsScreen(navController)
        }

    }
}

@Composable
fun currentRouteForBottomBar(navController: NavHostController): Boolean {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route in listOf(
        Screen.Home.route,
        Screen.Courses.route,
        Screen.Schedule.route
    )
}