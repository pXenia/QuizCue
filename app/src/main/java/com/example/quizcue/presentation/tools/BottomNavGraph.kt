package com.example.quizcue.presentation.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizcue.presentation.courses_screen.CoursesScreen
import com.example.quizcue.presentation.questionslearncreens.LearnCardScreen
import com.example.quizcue.presentation.questionslearncreens.QuestionsScreen
import com.example.quizcue.presentation.authentication.AuthenticationNavigationViewModel
import com.example.quizcue.presentation.authentication.login_screen.LoginScreen
import com.example.quizcue.presentation.authentication.register_screen.RegisterScreen
import com.example.quizcue.presentation.competition_screen.AddCompetitionDialog
import com.example.quizcue.presentation.competition_screen.ChoseAddingCompetitionDialog
import com.example.quizcue.presentation.competition_screen.CompetitionScreen
import com.example.quizcue.presentation.courses_screen.AddCourseDialog
import com.example.quizcue.presentation.editquestionscreen.EditQuestion
import com.example.quizcue.presentation.homescreen.HomeScreen
import com.example.quizcue.presentation.quizscreen.QuizScreen
import com.example.quizcue.presentation.quizscreen.QuizViewModel
import com.example.quizcue.presentation.quizscreen.ResultQuizScreen
import com.example.quizcue.presentation.schedule_screen.ScheduleScree

@Composable
fun BottomNavGraph(navController: NavHostController,
                   authenticationNavigationViewModel: AuthenticationNavigationViewModel = hiltViewModel(),
                   quizViewModel: QuizViewModel = hiltViewModel()
) {
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
        composable(route = Screen.QuizNavGraph.route + "?courseId={courseId}",
            arguments = listOf(
                navArgument(
                    name = "courseId"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) {
            QuizNavGraph(navController = rememberNavController(), parentNavController = navController)
        }
        composable(route = Screen.Competition.route + "?competitionId={competitionId}",
            arguments = listOf(
                navArgument(
                    name = "competitionId"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) {
            CompetitionScreen(navController)
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
        composable(route = Screen.LearnCard.route + "?courseId={courseId}",
            arguments = listOf(
                navArgument(
                    name = "courseId"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) {
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
        dialog(
            route = Screen.AddCompetitionDialog.route,
        ) {
            ChoseAddingCompetitionDialog( navController = navController)
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