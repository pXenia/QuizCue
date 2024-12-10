package com.example.quizcue.presentation.tools

import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizcue.presentation.courses_screen.CoursesScreen
import com.example.quizcue.presentation.questionslearncreens.LearnCardScreen
import com.example.quizcue.presentation.questionslearncreens.QuestionsScreen
import com.example.quizcue.presentation.authentication.AuthenticationNavigationViewModel
import com.example.quizcue.presentation.authentication.login_screen.LoginScreen
import com.example.quizcue.presentation.authentication.register_screen.RegisterScreen
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
fun AppNavGraph(
    navController: NavHostController,
    authenticationNavigationViewModel: AuthenticationNavigationViewModel = hiltViewModel(),
) {
    NavHost(
        navController = navController,
        startDestination = if (authenticationNavigationViewModel.isLoggedInState.value)
            Screen.Login.route else Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Courses.route) {
            CoursesScreen(navController)
        }
        composable(Screen.Schedule.route) {
            ScheduleScree()
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.EditQuestion.route+ "?courseId={courseId}" + "?questionId={questionId}",
           listOf(
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
        composable(Screen.Questions.route+ "?courseId={courseId}",
            listOf(
                navArgument(
                    name = "courseId"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            QuestionsScreen(navController = navController)
        }

        composable(Screen.LearnCard.route + "?courseId={courseId}",
            listOf(
                navArgument(
                    name = "courseId"
                ) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) {
            LearnCardScreen(navController)
        }

        navigation(
            startDestination = Screen.Quiz.route,
            route = Screen.QuizNavGraph.route + "?courseId={courseId}",
            arguments = listOf(
                navArgument(name = "courseId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            composable(Screen.Quiz.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.QuizNavGraph.route + "?courseId={courseId}")
                }
                val quizViewModel = hiltViewModel<QuizViewModel>(parentEntry)

                QuizScreen(
                    navController = navController,
                    quizViewModel = quizViewModel
                )
            }
            composable(Screen.ResultQuiz.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.QuizNavGraph.route + "?courseId={courseId}")
                }
                val quizViewModel = hiltViewModel<QuizViewModel>(parentEntry)

                ResultQuizScreen(
                    navController = navController,
                    quizViewModel = quizViewModel
                )
            }
        }
        navigation(
            route = Screen.CompetitionNavGraph.route,
            startDestination = Screen.Competition.route) {
            composable(Screen.Competition.route + "?competitionId={competitionId}",
                listOf(
                    navArgument(
                        name = "competitionId"
                    ) {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )) {
                CompetitionScreen(navController)
            }
        }
        dialog(
            route = Screen.AddCourse.route
        ) {
            AddCourseDialog(navController)
        }
        dialog(
            route = Screen.AddCompetitionDialog.route
        ) {
            ChoseAddingCompetitionDialog(navController)
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
