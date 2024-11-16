package com.example.quizcue.presentation.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quizcue.domain.model.Course
import com.example.quizcue.presentation.tools.Screen

@Composable
fun CourseCard(
    navController: NavController,
    course: Course,
    textColor: Color,
    trackColor: Color,
    cardColor: CardColors
) {
    OutlinedCard(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        border = BorderStroke(1.dp, trackColor),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = cardColor,
        onClick = {
            navController.navigate(Screen.Questions.route+"?courseId=${course.id}")
        }
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = course.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = course.description,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            ProgressCircle(
                textColor =  textColor,
                trackColor = trackColor,
                progress = 0.7f)
        }
    }
}

@Composable
fun ProgressCircle(
    textColor: Color,
    trackColor: Color,
    progress: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(80.dp)
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            color = textColor,
            strokeWidth = 5.dp,
            trackColor = trackColor,
        )
        Text(
            text = "${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            color = textColor,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
