package com.example.quizcue.presentation.edit_question_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditQuestion(
    navController: NavController,
    questionViewModel: EditQuestionViewModel = hiltViewModel()
) {
    val questionsState by questionViewModel.questionsFlow.collectAsState()
    var answer = questionViewModel.answerGenerationResult.collectAsState().value ?: ""
    var hint = questionViewModel.hintGenerationResult.collectAsState().value ?: ""

    var question by remember { mutableStateOf("") }
    Scaffold(
        floatingActionButton = {
            Row {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                    }) {
                    Icon(Icons.Filled.Done, contentDescription = "Done")
                }
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        questionViewModel.generateHint(answer)
                    }) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = "AutoAwesome")
                }
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(15.dp),
                    onClick = {
                        questionViewModel.generateAnswer(question)
                    }) {
                    Icon(Icons.Filled.AutoAwesome, contentDescription = "AutoAwesome")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(15.dp)
                .fillMaxSize()
        ) {
            Spacer(
                modifier = Modifier
                    .padding(10.dp)
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Вопрос",
                    )
                },
                value = question,
                onValueChange = { question = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(5.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text(text = "Подсказка") },
                value = hint,
                onValueChange = { hint = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(5.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary
            )
            TextField(
                modifier = Modifier
                    .fillMaxSize(),
                value = answer,
                placeholder = { Text(text = "Ответ") },
                onValueChange = { answer = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
            )
        }
    }
}


