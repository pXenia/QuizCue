package com.example.quizcue.presentation.elements

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.quizcue.presentation.schedule_screen.QuestionTitle

@Composable
fun QuestionsList(questionTitles: List<QuestionTitle>) {
    val heightScr = LocalConfiguration.current.screenHeightDp.dp
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightScr),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            items = questionTitles,
        ) { question ->
            QuestionCard(text = question.description)
            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            if (question == questionTitles.last()) {
                Spacer(
                    modifier = Modifier
                        .height(55.dp)
                )
            }
        }
    }
}