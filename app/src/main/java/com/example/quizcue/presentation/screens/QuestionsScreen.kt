package com.example.quizcue.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.quizcue.presentation.screens.schedule_screen.QuestionTitle
import com.example.quizcue.presentation.screens.schedule_screen.QuestionsList
import com.example.quizcue.presentation.tools.Screen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuestionsScreen(
    navController: NavController
) {
    val heightScr = LocalConfiguration.current.screenHeightDp.dp
    val random = Random()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val questions = List(20) {
        val month = 7
        val day = random.nextInt(15) + 1
        val calendar = Calendar.getInstance()
        calendar.set(2024, month, day)

        QuestionTitle(
            date = calendar.time,
            description = "Question ${it + 1}: Random question text here."
        )
    }
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
                    onClick = { /*TODO*/ }) {
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
            QuestionsList(
                questionTitles = questions
            )

        }
    }
}

@Composable
fun QuestionCard(
    text: String) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        colors = CardDefaults.cardColors(Color.Transparent),
        onClick = { /* TODO */ }
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Default.QuestionMark,
                "QuestionMark",
                tint = MaterialTheme.colorScheme.primary)
            Text(
                modifier = Modifier.padding(start = 14.dp),
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}