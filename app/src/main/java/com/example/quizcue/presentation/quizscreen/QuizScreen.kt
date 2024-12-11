package com.example.quizcue.presentation.quizscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quizcue.presentation.competition_screen.uiState
import com.example.quizcue.presentation.tools.CircularProgress
import com.example.quizcue.presentation.tools.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    quizViewModel: QuizViewModel
) {
    val uiState by quizViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Тест") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        content = { Icon(Icons.Filled.ArrowBackIosNew, "Назад") }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    quizViewModel.scoreCalculate()
                    navController.navigate(Screen.ResultQuiz.route)
                },
                shape = RoundedCornerShape(15.dp),
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                content = {
                    Icon(Icons.Filled.Done, "Следующий вопрос")
                }
            )
        }
    ) { padding ->
        if (uiState.isEmpty()){
            CircularProgress()
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 25.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(uiState) { quizState ->
                    QuizContent(
                        quizUIState = quizState,
                        onSelectAnswer = { selectedAnswer ->
                            quizViewModel.processAnswer(
                                questionText = quizState.questionText,
                                selectedAnswer = selectedAnswer
                            )
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun QuizContent(
    quizUIState: QuizUIState,
    onSelectAnswer: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = quizUIState.questionText,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        quizUIState.answers.forEach { answerText ->
            AnswerQuizCard(
                answerText = answerText,
                isSelected = quizUIState.selectedAnswer == answerText,
                onSelectAnswer = { onSelectAnswer(answerText) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



@Composable
fun AnswerQuizCard(
    answerText: String,
    isSelected: Boolean,
    onSelectAnswer: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        colors = CardDefaults.cardColors(Color.Transparent),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelectAnswer
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = answerText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
