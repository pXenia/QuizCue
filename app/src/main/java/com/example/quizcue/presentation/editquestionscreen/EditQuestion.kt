package com.example.quizcue.presentation.editquestionscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.presentation.tools.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestion(
    navController: NavController,
    questionViewModel: EditQuestionViewModel = hiltViewModel()
) {
    val uiState by questionViewModel.uiState.collectAsState()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("")},
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
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    questionViewModel.onEvent(EditQuestionEvent.SaveQuestion)
                    navController.popBackStack()
                },
                shape = RoundedCornerShape(15.dp),
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                content = { Icon(Icons.Filled.Done, "Сохранить вопрос") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 25.dp)
                .fillMaxSize()
        ) {
            // поле для ввода вопроса
            TextFieldForQuestion(
                title = "Вопрос:",
                value = uiState.text,
                onValueChange = {questionViewModel.onEvent(EditQuestionEvent.EnteredTextQuestion(it))},
                modifier = Modifier
                    .padding(bottom = 7.dp),
            )

            // поле для ввода подсказки
            TextFieldForQuestion(
                title = "Подсказка:",
                value = uiState.hint,
                onValueChange = {questionViewModel.onEvent(EditQuestionEvent.EnteredHintQuestion(it))},
                modifier = Modifier
                    .padding(vertical = 7.dp),
                icon = Icons.Outlined.AutoAwesome,
                onClick = {
                    if (uiState.answer != "")
                    questionViewModel.generateHint()
                    else
                        Toast.makeText(context, "Сначала добавьте ответ на вопрос",Toast.LENGTH_SHORT).show()
                }
            )

            // поле для ввода ответа
            TextFieldForQuestion(
                title = "Ответ:",
                value = uiState.answer,
                onValueChange = {questionViewModel.onEvent(EditQuestionEvent.EnteredAnswerQuestion(it))},
                modifier = Modifier
                    .padding(vertical = 7.dp),
                icon = Icons.Outlined.AutoAwesome,
                onClick = {
                    if (uiState.text != "")
                        questionViewModel.generateAnswer()
                    else
                        Toast.makeText(context, "Сначала введите вопрос",Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun TextFieldForQuestion(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    icon: ImageVector? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {

        // заголовок поля
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                modifier = Modifier
                    .weight(1f),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            if (icon != null) {
                IconButton(
                    onClick = onClick,
                    content = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }

        }

        if (icon == null){
            Spacer(modifier = Modifier.height(10.dp))
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Добавить ...") },
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences
            ),
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )

        )
    }

}




