package com.example.quizcue.presentation.coursesscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                text = stringResource(R.string.add_course),
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
                            text = stringResource(R.string.title),
                        )
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .background(Color.Transparent),
                    value = courseViewModel.description.value,
                    onValueChange = {
                        courseViewModel.onEvent(EditCourseEvent.EnteredDescriptionCourse(it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.description),
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
                    text = stringResource(R.string.ok),
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
                    text = stringResource(R.string.cansel),
                )
            }
        }
    )

}