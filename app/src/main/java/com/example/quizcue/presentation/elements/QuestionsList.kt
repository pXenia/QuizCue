package com.example.quizcue.presentation.elements

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizcue.domain.model.Question
import com.example.quizcue.presentation.tools.Screen
import com.example.quizcue.ui.theme.studyLight

@Composable
fun QuestionsList(
    questionList: List<Question>,
    navController: NavController,
    modifier: Modifier,
    onAddToFavorites: (Question) -> Unit,
    onDeleteQuestion: (Question) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        items(
            items = questionList,
            key = {
                it.id
            }) { question ->
            QuestionCard(
                question = question,
                onClick = {
                    navController.navigate(Screen.EditQuestion.route + "?courseId=${question.course}" + "?questionId=${question.id}")
                },
                iconColor = if (question.isStudied) studyLight else MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth(),
                onAddToFavorites = onAddToFavorites,
                onDeleteQuestion = onDeleteQuestion
            )
            if (question.id == questionList.last().id)
                Spacer(modifier = Modifier.height(60.dp))
        }
    }
}