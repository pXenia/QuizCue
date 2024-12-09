package com.example.quizcue.presentation.competition_screen

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.R
import com.example.quizcue.domain.model.User
import com.example.quizcue.presentation.tools.Screen
import com.google.android.play.integrity.internal.c
import com.google.android.play.integrity.internal.w
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitionScreen(
    navController: NavController,
    competitionViewModel: CompetitionViewModel = hiltViewModel()
) {
    val uiState by competitionViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.Home.route)
                        },
                        content = {
                            Icon(Icons.Filled.ArrowBackIosNew, "Назад")
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            if (uiState.competitionId == "") {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.AddCompetitionDialog.route)
                    },
                    shape = RoundedCornerShape(15.dp),
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    content = { Icon(Icons.Filled.Add, "Добавить") }
                )
            } else {
                FloatingActionButton(
                    onClick = {
                        competitionViewModel.deleteCompetition(uiState.competitionId)
                        navController.navigate(Screen.Home.route)
                    },
                    shape = RoundedCornerShape(15.dp),
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    content = {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.padding(5.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = "Завершить"
                            )
                            Text(
                                text = "Завершить",
                                modifier = Modifier.padding(5.dp),
                            )
                        }
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 25.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.competitionId != ""){
                CompetitionContent(
                    uiState = uiState,
                    onClick = {competitionViewModel.getCompetitionById(uiState.competitionId)})
            } else {
                Text("Добавьте соревнование")
            }
        }
    }
}

@Composable
fun CompetitionContent(
    uiState: uiState,
    onClick: () -> Unit
) {
    val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(uiState.date)
    val user1Score = uiState.user1TestScore
    val user2Score = uiState.user2TestScore

    OutlinedCard(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
        colors = CardDefaults.cardColors(Color.Transparent),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Дата окончания ${date}")
            Text("Приз: ${uiState.prize}")
        }
    }

    if (uiState.user1 != null) {
        UserCard(
            score = uiState.user1TestScore,
            user = uiState.user1,
            cardColor = MaterialTheme.colorScheme.primary
        )
    }

    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { user1Score / (user1Score+user2Score).toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .height(25.dp),
            trackColor = MaterialTheme.colorScheme.tertiary,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(titleScore(user1Score), style = MaterialTheme.typography.titleLarge)
            Text(titleScore(user2Score), style = MaterialTheme.typography.titleLarge)
        }
    }

    if (uiState.user2?.competitionId != "" && uiState.user2 != null) {
        UserCard(
            score = uiState.user2TestScore,
            user = uiState.user2,
            cardColor = MaterialTheme.colorScheme.tertiary
        )
    } else {
        CardWithoutOpponent(
            competitionId = uiState.competitionId,
            onClick = onClick
        )
    }

    Spacer(modifier = Modifier.height(30.dp))
}

@Composable
fun UserCard(
    score: Int,
    user: User,
    cardColor: Color
) {
    OutlinedCard(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(cardColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = 30.dp,
                    horizontal = 20.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = if (user.photo != null) {
                    BitmapPainter(user.photo.asImageBitmap())
                } else
                    painterResource(id = R.drawable.koshka),
                contentDescription = "Фото пользователя",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = user.name, style = MaterialTheme.typography.headlineMedium)
                Text("Баллы: ${score}", style = MaterialTheme.typography.titleSmall)
            }

        }
    }
}

@Composable
fun CardWithoutOpponent(
    competitionId: String,
    onClick: () -> Unit,
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
            elevation = CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = "Чтобы пригласить друга, скопируйте код ниже и отправьте ему!",
                style = MaterialTheme.typography.titleSmall.copy(
                    textAlign = TextAlign.Center
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = competitionId,
                    style = MaterialTheme.typography.titleMedium,
                )
                IconButton(onClick = {
                    clipboardManager.setText(AnnotatedString(competitionId))
                    Toast.makeText(context, "Ключ скопирован в буфер обмена", Toast.LENGTH_SHORT)
                        .show()
                }) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Копировать ключ"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier.padding(5.dp),
                imageVector = Icons.Default.Update,
                contentDescription = "Update"
            )
            Text(
                text = "Обновить",
                modifier = Modifier.padding(5.dp),
            )
        }
    }
}

fun titleScore(score: Int): String {
    return when {
        score % 100 in 11..19 -> "$score баллов"
        score % 10 == 1 -> "$score балл"
        score % 10 in 2..4 -> "$score балла"
        else -> "$score баллов"
    }
}
