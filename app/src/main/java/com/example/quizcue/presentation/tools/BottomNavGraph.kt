package com.example.quizcue.presentation.tools

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.example.quizcue.presentation.courses_screen.CoursesScreen
import com.example.quizcue.presentation.questions_and_learn_card_screen.LearnCardScreen
import com.example.quizcue.presentation.questions_and_learn_card_screen.QuestionsScreen
import com.example.quizcue.presentation.authentication.AuthenticationNavigationViewModel
import com.example.quizcue.presentation.authentication.login_screen.LoginScreen
import com.example.quizcue.presentation.authentication.register_screen.RegisterScreen
import com.example.quizcue.presentation.courses_screen.AddCourseDialog
import com.example.quizcue.presentation.edit_question_screen.EditQuestion
import com.example.quizcue.presentation.schedule_screen.ScheduleScree

@Composable
fun BottomNavGraph(navController: NavHostController,
                   authenticationNavigationViewModel: AuthenticationNavigationViewModel = hiltViewModel()) {
    NavHost(
        navController = navController,
        startDestination = if (authenticationNavigationViewModel.isLoggedInState.value)
            Screen.Login.route
        else
            Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.Courses.route) {
            CoursesScreen(navController)
        }
        composable(route = Screen.Schedule.route) {
            ScheduleScree()
        }
        composable(route = Screen.Questions.route + "?courseId={courseId}",
            arguments = listOf(
                navArgument(
                    name = "courseId"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) {
            QuestionsScreen(navController)
        }
        composable(route = Screen.LearnCard.route) {
            LearnCardScreen(navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(route = Screen.EditQuestion.route+ "?courseId={courseId}" + "?questionId={questionId}",
            arguments = listOf(
                navArgument(
                    name = "courseId"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(
                    name = "questionId"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            EditQuestion(navController = navController)
        }
        dialog(
            route = Screen.AddCourse.route,
        ) {
            AddCourseDialog(navController = navController)
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