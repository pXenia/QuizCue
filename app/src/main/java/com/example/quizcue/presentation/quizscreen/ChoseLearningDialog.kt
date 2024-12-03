package com.example.quizcue.presentation.quizscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizcue.presentation.tools.Screen

@Composable
fun ChooseLearningDialog(
    course: String,
    navController: NavController
) {
    var selectedItem by remember { mutableStateOf(0) }
    AlertDialog(
        title = { Text(text = "Выберите спопсоб изучения") },
        text = {
            Column {
                ChoseItem(
                    title = "Повторить карточки",
                    isSelected = selectedItem == 0,
                    onSelected = { selectedItem = 0 })
                Spacer(modifier = Modifier.height(10.dp))
                ChoseItem(
                    title = "Пройти Тест",
                    isSelected = selectedItem == 1,
                    onSelected = { selectedItem = 1 }
                )
            }
        },
        onDismissRequest = {
            navController.navigateUp()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (selectedItem == 0)
                        navController.navigate(Screen.LearnCard.route+"?courseId=${course}")
                    else
                        navController.navigate(Screen.Quiz.route+"?courseId=${course}")

                }
            ) {
                Text("Ок")
            }
        }
    )
}


@Composable
fun ChoseItem(
    title: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        RadioButton(
            selected = isSelected,
            onClick = onSelected
        )
        Text(
            text = title,
            modifier = Modifier
                .clickable(onClick = onSelected)
        )
    }
}
