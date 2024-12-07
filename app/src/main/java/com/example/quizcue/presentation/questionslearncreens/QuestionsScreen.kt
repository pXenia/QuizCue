package com.example.quizcue.presentation.questionslearncreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.presentation.elements.QuestionsList
import com.example.quizcue.presentation.quizscreen.ChooseLearningDialog
import com.example.quizcue.presentation.tools.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionsScreen(
    navController: NavController,
    questionsViewModel: QuestionsScreensViewModel = hiltViewModel()
) {
    val questions by questionsViewModel.questions.collectAsState()
    val courseName by questionsViewModel.courseTitle
    val course = questionsViewModel.courseId

    var chooseLearningDialog by remember { mutableStateOf(false) }

    if (chooseLearningDialog) {
        ChooseLearningDialog(
            course = course,
            navController = navController
        )
    }

    questionsViewModel.updateLastTime()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        },
                        content = {
                            Icon(Icons.Filled.ArrowBackIosNew, "Назад")
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            QuestionsScreenBottomBar(
                onClickLearn = {
                    chooseLearningDialog = true
                },
                onClickAdd = {
                    navController.navigate(Screen.EditQuestion.route+ "?courseId=${course}" + "?questionId=${""}")
                },
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 25.dp)
                .fillMaxSize()
        ) {
            Text(text = courseName.uppercase(),
                modifier = Modifier.padding(bottom = 5.dp),
                style = MaterialTheme.typography.headlineMedium)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 2.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            QuestionsList(
                modifier = Modifier.fillMaxSize(),
                questionList = questions,
                navController = navController,
                onAddToFavorites = { question ->
                    questionsViewModel.addToFavorites(question)
                },
                onDeleteQuestion = { question ->
                    questionsViewModel.deleteQuestion(question)
                }
            )
        }
    }
}

@Composable
fun QuestionsScreenBottomBar(
    onClickLearn: () -> Unit,
    onClickAdd: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
    ) {
        OutlinedButton(
            onClick = onClickLearn,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(15.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                text = "Учить",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center
                ),
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        FloatingActionButton(
            onClick = onClickAdd,
            shape = RoundedCornerShape(15.dp),
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
}