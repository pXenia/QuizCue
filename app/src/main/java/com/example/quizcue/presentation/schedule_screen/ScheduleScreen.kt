package com.example.quizcue.presentation.schedule_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizcue.domain.model.CalendarData
import com.example.quizcue.domain.repository.CalendarRepository
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.IndicatorPosition
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.VerticalIndicatorProperties
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

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
                    label = "Баллы за тесты",
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
                    label = "Повторений вопросов",
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


