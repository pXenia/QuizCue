package com.example.quizcue.presentation.courses_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizcue.presentation.elements.CourseCard
import com.example.quizcue.presentation.tools.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CoursesScreen(
    navController: NavController,
    courseViewModel: CourseViewModel = hiltViewModel()
) {

    val scroll = rememberScrollState()
    val heightScr = LocalConfiguration.current.screenHeightDp.dp
    val courses = courseViewModel.courses.value
    val progress = courseViewModel.progress.value

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .verticalScroll(scroll)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = 1f - 10f * (scroll.value.toFloat() / scroll.maxValue)
                },
            text = "Курсы",
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(15.dp)),
                leadingIcon = {
                    IconButton(
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(Icons.Filled.Search,
                            "Search",
                            tint = MaterialTheme.colorScheme.primary)
                    }
                },
                value = "",
                onValueChange = {},
                shape = RoundedCornerShape(15.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    navController.navigate(Screen.AddCourse.route)
                }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")

            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.tertiary
        )
        LazyColumn(
            modifier = Modifier.height(heightScr*0.84f)
        ) {
            items(courses) {
                CourseCard(
                    navController= navController,
                    cardColor = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerHighest),
                    textColor = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.tertiary,
                    progress = progress[it.id] ?: 0f,
                    course = it
                )
                if (it == courses.last()) {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}