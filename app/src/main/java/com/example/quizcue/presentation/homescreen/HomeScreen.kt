package com.example.quizcue.presentation.homescreen

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.domain.model.Course
import com.example.quizcue.domain.model.User
import com.example.quizcue.presentation.competition_screen.uiState
import com.example.quizcue.presentation.courses_screen.CourseViewModel
import com.example.quizcue.presentation.elements.CourseCard
import com.example.quizcue.presentation.tools.ProfileImage
import com.example.quizcue.presentation.tools.Screen
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeScreenViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel = hiltViewModel()
) {
    homeViewModel.lastTimeCourse()
    val uiState by homeViewModel.uiState.collectAsState()
    val competitionDate by homeViewModel.competitionDate.collectAsState()
    val allQuestionsSize by homeViewModel.allQuestionsAmount.collectAsState()
    val favouriteQuestionsSize by homeViewModel.favouriteQuestionsAmount.collectAsState()
    val lastCourse by homeViewModel.lastCourse.collectAsState()
    val lastCourseProcess = courseViewModel.progress.value[lastCourse.id] ?: 0f
    val email = homeViewModel.email

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(Transparent),
                actions = {
                    LogoutButton(
                        onLogoutClick = {
                            homeViewModel.logout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0)
                            }
                        }
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        HomeContent(
            user = uiState,
            competitionDate = competitionDate,
            allQuestionsSize = allQuestionsSize,
            favouriteQuestionsSize = favouriteQuestionsSize,
            email = email,
            navController = navController,
            lastCourse = lastCourse,
            lastCourseProcess = lastCourseProcess
        )
    }
}

@Composable
fun LogoutButton(onLogoutClick: () -> Unit) {
    IconButton(onClick = onLogoutClick) {
        Icon(
            imageVector = Icons.Filled.Logout,
            contentDescription = "Выход"
        )
    }
}

@Composable
fun HomeContent(
    user: User,
    competitionDate: Long,
    allQuestionsSize: Int,
    favouriteQuestionsSize: Int,
    email: String,
    navController: NavController,
    lastCourse: Course,
    lastCourseProcess: Float
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        UserHeader(user, email)
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        Body(
            user = user,
            competitionDate = competitionDate,
            allQuestionsSize = allQuestionsSize,
            favouriteQuestionsSize = favouriteQuestionsSize,
            navController = navController
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        CourseCard(
            navController = navController,
            textColor = MaterialTheme.colorScheme.onPrimary,
            trackColor = MaterialTheme.colorScheme.tertiary,
            cardColor = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
            progress = lastCourseProcess,
            course = lastCourse
        )
    }
}

@Composable
fun UserHeader(user: User, email: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileImage(
            size = 130.dp,
            photo = user.photo)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = email,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun Body(user: User,
         competitionDate: Long,
         allQuestionsSize: Int,
         favouriteQuestionsSize: Int,
         navController: NavController) {
    val dateText = if (competitionDate != 0L)
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(competitionDate)
    else "Нет соревнований"

    Box(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .border(1.dp, MaterialTheme.colorScheme.tertiary, FloatingActionButtonDefaults.shape)
            .padding(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ActiveItem(
                icon = Icons.Default.Favorite,
                title = "Избранное",
                subtitle = "Всего вопросов: $favouriteQuestionsSize",
                onClick = { navController.navigate(Screen.Questions.route + "?courseId=favourite") }
            )
            ActiveItem(
                icon = Icons.Default.QuestionMark,
                title = "Все вопросы",
                subtitle = "Всего вопросов: $allQuestionsSize",
                onClick = { navController.navigate(Screen.Questions.route + "?courseId=all") }
            )
            ActiveItem(
                icon = Icons.Outlined.EmojiEvents,
                title = "Соревнование",
                subtitle = dateText,
                onClick = { navController.navigate(Screen.Competition.route + "?competitionId=${user.competitionId}")}
            )
        }
    }
}

@Composable
fun ActiveItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FloatingActionButton(
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.tertiary,
                FloatingActionButtonDefaults.shape
            ),
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(imageVector = icon, contentDescription = null)
        }
        Spacer(modifier = Modifier.fillMaxWidth(0.2f))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = subtitle, style = MaterialTheme.typography.titleSmall)
        }
    }
}
