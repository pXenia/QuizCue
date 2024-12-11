package com.example.quizcue.presentation.schedulescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizcue.R
import com.example.quizcue.domain.model.CalendarData
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScheduleScreen(
    scheduleViewModel: ScheduleViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val lastWeekData by scheduleViewModel.calendarDataLastWeek.collectAsState()
        val data by scheduleViewModel.calendarData.collectAsState()
        CalendarWithEvents(data)
        LineChartScreen(lastWeekData)
    }
}


@Composable
fun LineChartScreen(
    data: List<CalendarData>
) {
    if (data.isEmpty()) {
        Text(text = "Loading data...")
    } else {
        val sortedData = data.sortedBy { it.date }
        val quizScores = sortedData.map { it.quizScore.toDouble() }
        val repetitionNumbers = sortedData.map { it.repetitionNumber.toDouble() }
        val labels = sortedData.map { formatDate(it.date) }

        LineChart(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .fillMaxWidth(),
            labelProperties = LabelProperties(
                enabled = true,
                labels = labels
            ),
            data = listOf(
                Line(
                    label = stringResource(R.string.score_for_quiz),
                    values = quizScores,
                    color = SolidColor(MaterialTheme.colorScheme.primary),
                    curvedEdges = true,
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(Color.White),
                        strokeWidth = 4.dp,
                        radius = 7.dp,
                        strokeColor = SolidColor(MaterialTheme.colorScheme.primary),
                    )
                ),
                Line(
                    label = stringResource(R.string.repeat_numb),
                    values = repetitionNumbers,
                    color = SolidColor(MaterialTheme.colorScheme.secondary),
                    curvedEdges = true,
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(Color.White),
                        strokeWidth = 4.dp,
                        radius = 7.dp,
                        strokeColor = SolidColor(MaterialTheme.colorScheme.secondary),
                    )
                )
            ),
            curvedEdges = false
        )
    }

}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
    return sdf.format(Date(timestamp))
}


