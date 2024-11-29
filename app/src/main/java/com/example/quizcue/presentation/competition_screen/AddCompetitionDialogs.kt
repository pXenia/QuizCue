package com.example.quizcue.presentation.competition_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.presentation.tools.Screen


@Composable
fun ChoseAddingCompetitionDialog(
    navController: NavController,
) {
    var showAddCompetitionDialog by remember { mutableStateOf(false) }
    var showAddCompetitionByKeyDialog by remember { mutableStateOf(false) }

    if (showAddCompetitionDialog) {
        AddCompetitionDialog(
            navController = navController
        )
    }

    if (showAddCompetitionByKeyDialog) {
        AddCompetitionByKeyDialog(
            navController = navController
        )
    }

    AlertDialog(
        title = { Text(text = "Создание соревнования") },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    showAddCompetitionDialog = true
                }
            ) {
                Text("Создать")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showAddCompetitionByKeyDialog = true
                }
            ) {
                Text("Ввести Код")
            }
        }
    )
}

@Composable
fun AddCompetitionDialog(
    navController: NavController,
    competitionViewModel: CompetitionViewModel = hiltViewModel()
) {
    val uiState by competitionViewModel.uiState.collectAsState()
    var showForCompetitionDialog by remember { mutableStateOf(false) }
    val competitionKey by competitionViewModel.competitionKey

    if (showForCompetitionDialog) {
        KeyForCompetitionDialog(
            navController = navController,
            competitionKey = competitionKey,
        )
    }

    AlertDialog(
        title = { Text(text = "Создание соревнования") },
        text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .background(Color.Transparent),
                    value = uiState.prize,
                    onValueChange = {
                        competitionViewModel.onEvent(AddCompetitionEvent.EditCompetitionPrize(it))
                    },
                    placeholder = {
                        Text(
                            text = "Приз",
                        )
                    }
                )
            }
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    competitionViewModel.addCompetition()
                    showForCompetitionDialog = true
                }
            ) {
                Text("Создать ключ")
            }
        }
    )
}

@Composable
fun AddCompetitionByKeyDialog(
    navController: NavController,
    competitionViewModel: CompetitionViewModel = hiltViewModel(),
) {
    var addedCompetitionId by remember { mutableStateOf("") }


    AlertDialog(
        title = { Text(text = "Присоединение к соревнованию") },
        text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .background(Color.Transparent),
                    value = addedCompetitionId,
                    onValueChange = {
                        addedCompetitionId = it
                    },
                    placeholder = {
                        Text(
                            text = "Ключ",
                        )
                    }
                )
            }
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    competitionViewModel.addOpponent(competitionId = addedCompetitionId)
                    navController.navigate(Screen.Competition.route + "?competitionId=${addedCompetitionId}")
                }
            ) {
                Text("Присоединиться")
            }
        }
    )
}

@Composable
fun KeyForCompetitionDialog(
    navController: NavController,
    competitionKey: String
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    AlertDialog(
        title = { Text(text = "Создание соревнования") },
        onDismissRequest = {},
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = competitionKey,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    clipboardManager.setText(AnnotatedString(competitionKey))
                    Toast.makeText(context, "Ключ скопирован в буфер обмена", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Копировать ключ"
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    navController.navigate(Screen.Competition.route)
                }
            ) {
                Text("Ок")
            }
        }
    )
}

