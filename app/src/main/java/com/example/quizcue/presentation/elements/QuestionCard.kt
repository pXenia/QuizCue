package com.example.quizcue.presentation.elements

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizcue.domain.model.Question
import com.example.quizcue.presentation.tools.Screen

@Composable
fun QuestionCard(
    question: Question,
    navController: NavController) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        colors = CardDefaults.cardColors(Color.Transparent),
        onClick = {
            Log.d("QuestionID_1", question.id)
            navController.navigate(Screen.EditQuestion.route+"?questionId=${question.id}")
        }
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.QuestionMark,
                "QuestionMark",
                tint = MaterialTheme.colorScheme.primary)
            Text(
                modifier = Modifier.padding(start = 14.dp),
                text = question.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}