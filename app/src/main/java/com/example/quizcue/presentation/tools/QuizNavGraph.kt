package com.example.quizcue.presentation.tools

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.quizcue.presentation.quizscreen.QuizScreen
import com.example.quizcue.presentation.quizscreen.QuizViewModel
import com.example.quizcue.presentation.quizscreen.ResultQuizScreen

@Composable
fun QuizNavGraph(
    navController: NavHostController,
    parentNavController: NavHostController,
    quizViewModel: QuizViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Quiz.route
    ) {
        composable(
            route = Screen.Quiz.route
        ) {
            quizViewModel.createQuiz()
            QuizScreen(navController, quizViewModel)
        }
        composable(route = Screen.ResultQuiz.route) {
            ResultQuizScreen(
                parentNavController = parentNavController,
                navController = navController,
                quizViewModel = quizViewModel
            )
        }
    }
}
