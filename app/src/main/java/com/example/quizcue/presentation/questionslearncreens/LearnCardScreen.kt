package com.example.quizcue.presentation.questionslearncreens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.domain.model.Question
import com.example.quizcue.presentation.elements.LearnCard
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnCardScreen(
    navController: NavController,
    questionViewModel: QuestionsScreensViewModel = hiltViewModel(),
){
    val questions by questionViewModel.questions.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        content = {
                            Icon(Icons.Filled.ArrowBackIosNew, "Назад")
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Transparent
                )
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 25.dp)
                .fillMaxSize()
        ){
            if (questions.isEmpty()) {
            CircularProgressIndicator()
            } else {
               SwipeCard(
                   questions = questions
               )
            }
        }
    }
}
@Composable
fun SwipeCard(
    swipeThreshold: Float = 400f,
    sensitivityFactor: Float = 3f,
    questions: List<Question>,
    questionViewModel: QuestionsScreensViewModel = hiltViewModel(),
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

    var currentQuestion by remember { mutableStateOf(cardList.first()) }

    LaunchedEffect(dismissRight, dismissLeft) {
        if (dismissRight || dismissLeft) {
            delay(300)
            cardList = cardList.drop(1)
            if (cardList.isNotEmpty()) {
                currentQuestion = cardList.first()
            }
            offset = 0f
            dismissRight = false
            dismissLeft = false
        }
    }
    Column{
        Row(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 20.dp)
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
                    questionViewModel.updateQuestionIsStudied(
                        currentQuestion,
                        dismissRight
                    )
                    if (dismissRight) {
                        questionViewModel.updateStat()
                    }
                }) { change, dragAmount ->
                    if (cardList.isNotEmpty()) {
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
                    style = MaterialTheme.typography.displaySmall.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                key(currentQuestion.id) {
                    LearnCard(question = currentQuestion)
                }
            }
        }
    }
}


