package com.example.quizcue.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LearnCardScreen(navController: NavController){
    val questions = listOf(
        Question(
            question = "What is the capital of France?",
            hint = "It's known as the City of Light.",
            answer = "Paris"
        ),
        Question(
            question = "What is the largest planet in our solar system?",
            hint = "It's a gas giant.",
            answer = "Jupiter"
        ),
        Question(
            question = "Who wrote 'Hamlet'?",
            hint = "An English playwright, often called the Bard.",
            answer = "William Shakespeare"
        ),
        Question(
            question = "What is the chemical symbol for water?",
            hint = "Two hydrogen atoms and one oxygen atom.",
            answer = "H2O"
        ),
        Question(
            question = "What year did the Titanic sink?",
            hint = "It happened in the early 20th century.",
            answer = "1912"
        ),
        Question(
            question = "What is the smallest prime number?",
            hint = "It's also an even number.",
            answer = "2"
        ),
        Question(
            question = "Which element has the atomic number 6?",
            hint = "It's essential for life.",
            answer = "Carbon"
        ),
        Question(
            question = "Who painted the Mona Lisa?",
            hint = "An Italian Renaissance artist.",
            answer = "Leonardo da Vinci"
        ),
        Question(
            question = "What is the hardest natural substance on Earth?",
            hint = "It's often used in jewelry.",
            answer = "Diamond"
        ),
        Question(
            question = "What is the longest river in the world?",
            hint = "It flows through South America.",
            answer = "Amazon River"
        )
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(Transparent),
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(
                            imageVector = Icons.Filled.ChevronLeft,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        SwipeCard(questions = questions)
    }
}
@Composable
fun SwipeCard(
    swipeThreshold: Float = 400f,
    sensitivityFactor: Float = 3f,
    questions: List<Question>
) {
    var offset by remember { mutableStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density
    var cardList by remember { mutableStateOf(questions) }
    var repeat by remember { mutableStateOf(0) }
    var ready by remember { mutableStateOf(0) }
    val onSwipeLeft = { repeat++ }
    val onSwipeRight = { ready++ }
    LaunchedEffect(dismissRight, dismissLeft) {
        if (dismissRight || dismissLeft) {
            delay(300)
            if (cardList.isNotEmpty()) {
                cardList = cardList.drop(1)
            }
            offset = 0f
            dismissRight = false
            dismissLeft = false
        }
    }
    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 30.dp, bottom = 20.dp)
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(15.dp))
                .padding(10.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = "Повторить: $repeat",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Знаю: $ready",
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Box(modifier = Modifier
            .offset { IntOffset(offset.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onDragEnd = {
                    if (offset > swipeThreshold) {
                        dismissRight = true
                        onSwipeRight.invoke()
                    } else if (offset < -swipeThreshold) {
                        dismissLeft = true
                        onSwipeLeft.invoke()
                    } else {
                        offset = 0f
                    }
                }) { change, dragAmount ->
                    if (questions.isNotEmpty()) {
                        offset += (dragAmount / density) * sensitivityFactor
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
            }
            .graphicsLayer(
                alpha = 1f - animateFloatAsState(if (dismissRight || dismissLeft) 1f else 0f).value,
                rotationZ = animateFloatAsState(offset / 50).value
            ),
            contentAlignment = Alignment.Center) {
            if (cardList.isEmpty()) {
                Text(
                    "Нет доступных карточек",
                    modifier = Modifier.fillMaxSize(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        textAlign = TextAlign.Center
                    )
                )
            } else {
                key(cardList.first().question) {
                    LearnCard(question = cardList.first())
                }
            }
        }
    }
}

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
                    text = question.question,
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
data class Question(
    val question: String,
    val hint: String,
    val answer: String
)
