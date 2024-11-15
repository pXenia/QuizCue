package com.example.quizcue.presentation.courses_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.R
import com.example.quizcue.presentation.tools.Screen

@Composable
fun AddCourseDialog(
    navController: NavController,
    courseViewModel: CourseViewModel = hiltViewModel(),
) {
    AlertDialog(
        onDismissRequest = { navController.navigateUp() },
        title = {
            Text(
                text = "Добавить курс",
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .background(Color.Transparent),
                    value = courseViewModel.name.value,
                    onValueChange = {
                        courseViewModel.onEvent(EditCourseEvent.EnteredTextCourse(it))
                    },
                    placeholder = {
                        Text(
                            text = "Название",
                        )
                    }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .background(Color.Transparent),
                    value = courseViewModel.description.value,
                    onValueChange = {
                        courseViewModel.onEvent(EditCourseEvent.EnteredTextCourse(it))
                    },
                    placeholder = {
                        Text(
                            text = "Описание",
                        )
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                {
                    courseViewModel.onEvent(EditCourseEvent.SaveCourse)
                    navController.navigate(Screen.Courses.route)
                }
            ) {
                Text(
                    text = "Ок",
                )
            }
        },
        dismissButton = {
            TextButton(
                {
                    navController.navigate(Screen.Courses.route)
                }
            ) {
                Text(
                    text = "Отмена",
                )
            }
        }
    )

}