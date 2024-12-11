package com.example.quizcue.presentation.competitionscreen

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.quizcue.R
import com.example.quizcue.presentation.tools.Screen
import java.util.Locale


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
        title = { Text(text = stringResource(R.string.add_competition)) },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    showAddCompetitionDialog = true
                }
            ) {
                Text(stringResource(R.string.create))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showAddCompetitionByKeyDialog = true
                }
            ) {
                Text(stringResource(R.string.add_code))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCompetitionDialog(
    navController: NavController,
    competitionViewModel: CompetitionViewModel = hiltViewModel()
) {
    val uiState by competitionViewModel.uiState.collectAsState()
    var showForCompetitionDialog by remember { mutableStateOf(false) }
    val competitionKey by competitionViewModel.competitionKey

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showForCompetitionDialog) {
        KeyForCompetitionDialog(
            navController = navController,
            competitionKey = competitionKey,
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        competitionViewModel.onEvent(AddCompetitionEvent.EditCompetitionDate(selectedDate))
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cansel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Основной диалог
    AlertDialog(
        title = { Text(text = stringResource(R.string.creation_competition)) },
        text = {
            Column {

                OutlinedTextField(
                    modifier = Modifier
                        .background(Color.Transparent),
                    value = if (uiState.date != 0L) {
                        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(uiState.date)
                    } else {
                        stringResource(R.string.Choose_date)
                    },
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text(text = stringResource(R.string.date)) },
                    leadingIcon = {
                        IconButton(
                            onClick = { showDatePicker = true }
                        ) { Icon(Icons.Default.CalendarToday, null) }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .background(Color.Transparent),
                    value = uiState.prize,
                    onValueChange = {
                        competitionViewModel.onEvent(AddCompetitionEvent.EditCompetitionPrize(it))
                    },
                    placeholder = { Text(text = stringResource(R.string.prise)) }
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
                Text(stringResource(R.string.create_key))
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
        title = { Text(text = stringResource(R.string.add_opponent)) },
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
                            text = stringResource(R.string.key),
                        )
                    }
                )
            }
        },
        onDismissRequest = {navController.navigateUp()},
        confirmButton = {
            TextButton(
                onClick = {
                    competitionViewModel.addOpponent(competitionId = addedCompetitionId)
                    navController.navigate(Screen.Competition.route + "?competitionId=${addedCompetitionId}"){
                        popUpTo(navController.graph.findStartDestination().id)
                    }
                }
            ) {
                Text(stringResource(R.string.join))
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
        title = { Text(text = stringResource(R.string.add_competition)) },
        onDismissRequest = {
            navController.navigate(Screen.Competition.route + "?competitionId=${competitionKey}") {
                popUpTo(Screen.Competition.route) { inclusive = true }
            }
        },
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
                    Toast.makeText(context, context.getString(R.string.copy_key), Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    navController.navigate(Screen.Competition.route + "?competitionId=${competitionKey}"){
                        popUpTo(navController.graph.findStartDestination().id)
                    }
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}

