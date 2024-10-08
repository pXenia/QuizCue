package com.example.quizcue.presentation.tools

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.quizcue.presentation.CoursesScreen
import com.example.quizcue.presentation.LearnCardScreen
import com.example.quizcue.presentation.QuestionsScreen
import com.example.quizcue.presentation.authentication.AuthenticationNavigationViewModel
import com.example.quizcue.presentation.authentication.login_screen.LoginScreen
import com.example.quizcue.presentation.authentication.register_screen.RegisterScreen
import com.example.quizcue.presentation.schedule_screen.ScheduleScree

@Composable
fun BottomNavGraph(navController: NavHostController,
                   authenticationNavigationViewModel: AuthenticationNavigationViewModel = hiltViewModel()){
    NavHost(navController = navController,
        startDestination = if (authenticationNavigationViewModel.isLoggedInState.value)
            Screen.Login.route
        else
            Screen.Home.route
    ) {
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
        composable(route = Screen.LearnCard.route){
            LearnCardScreen(navController)
        }
        composable(route = Screen.Login.route){
            LoginScreen(navController)
        }
        composable(route = Screen.Register.route){
            RegisterScreen(navController)
        }
//        composable(route = Screen.EditQuestion.route){
//            EditQuestion(navController)
//        }
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