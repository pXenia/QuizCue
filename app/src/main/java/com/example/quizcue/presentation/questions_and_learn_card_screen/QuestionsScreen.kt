package com.example.quizcue.presentation.questions_and_learn_card_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.presentation.edit_question_screen.EditQuestionViewModel
import com.example.quizcue.presentation.elements.QuestionsList
import com.example.quizcue.presentation.tools.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuestionsScreen(
    navController: NavController,
    questionViewModel: EditQuestionViewModel = hiltViewModel()
) {
    val questions = questionViewModel.questions.value
    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    )
                    .fillMaxWidth(),
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                    onClick = { navController.navigate(Screen.LearnCard.route) }) {
                    Text(
                        text = "Учить",
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(15.dp),
                    onClick = { navController.navigate(Screen.EditQuestion.route+"?questionId=${""}") }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 5.dp,
                horizontal = 20.dp
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                text = "Название курса",
                style = MaterialTheme.typography.headlineMedium,
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            QuestionsList(questionList = questions, navController = navController)
        }
    }
}
