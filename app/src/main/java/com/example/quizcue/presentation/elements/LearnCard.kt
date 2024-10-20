package com.example.quizcue.presentation.elements

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.quizcue.domain.model.Question

@Composable
fun LearnCard(
    question: Question,
){
    var hintState by remember { mutableStateOf(false) }
    var expandedState by remember { mutableStateOf(false) }
    val rotationStateDropDown by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )
    val rotationStateQuestionMark by animateFloatAsState(
        targetValue = if (hintState) 180f else 0f
    )
    val scroll = rememberScrollState()
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scroll)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotationStateQuestionMark),
                    onClick = {
                        hintState = !hintState
                    }) {
                    Icon(
                        imageVector = Icons.Default.QuestionMark,
                        contentDescription = "QuestionMark",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = question.text,
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotationStateDropDown),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (hintState) {
                Text(
                    text = question.hint,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium

                )
            }
            if (expandedState) {
                Text(
                    text = question.answer,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

    }
}