package com.example.quizcue.presentation.schedule_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quizcue.domain.model.CalendarData
import com.example.quizcue.presentation.elements.QuestionsList
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CalendarWithEvents(
    data: List<CalendarData>
) {
    val currentMonth = remember { mutableStateOf(Calendar.getInstance()) }
    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2)

    HorizontalPager(
        count = Int.MAX_VALUE,
        state = pagerState,
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth(),
    ) { page ->
        val month = (currentMonth.value.clone() as Calendar).apply {
            add(Calendar.MONTH, page - Int.MAX_VALUE / 2)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 5.dp),
                text = "${month.get(Calendar.DAY_OF_MONTH)} ${
                    month.getDisplayName(
                        Calendar.MONTH,
                        Calendar.SHORT,
                        Locale.getDefault()
                    )
                } ${month.get(Calendar.YEAR)}",
                style = MaterialTheme.typography.bodyLarge,
            )
            CalendarGrid(data.map { it.date }, month)
        }
    }
}

@Composable
fun CalendarGrid(
    date: List<Long>,
    currentMonth: Calendar,
) {
    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfMonth = (currentMonth.clone() as Calendar).apply {
        set(Calendar.DAY_OF_MONTH, 0)
    }
    val startingDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1
    val today = Calendar.getInstance()
    val isCurrentMonth = today.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
            today.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR)

    val paddingDaysBefore = List(startingDayOfWeek) { 0 }
    val paddingDaysAfter = List((7 - (startingDayOfWeek + daysInMonth) % 7) % 7) { 0 }
    val allDays = paddingDaysBefore + (1..daysInMonth).toList() + paddingDaysAfter

    LazyVerticalGrid(columns = GridCells.Fixed(7)) {
        items(allDays.size) { index ->
            val day = allDays[index]
            val isSelectedDay = remember { mutableStateOf(-1) }

            val eventsForDay = date.filter { date ->
                val eventDate = Calendar.getInstance().apply { timeInMillis = date }
                eventDate.get(Calendar.DAY_OF_MONTH) == day &&
                        eventDate.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
                        eventDate.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR)
            }

            val isToday = isCurrentMonth && day == today.get(Calendar.DAY_OF_MONTH)
            val textColor = if (day <= 0) Color.Transparent else MaterialTheme.colorScheme.tertiary
            val currentDay = if (day > 0) day.toString() else ""

            Surface(
                modifier = Modifier
                    .padding(2.dp)
                    .height(50.dp)
                    .width(45.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.background,
                border = when {
                    isToday -> BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                    else -> BorderStroke(0.2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentDay,
                        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                        color = textColor
                    )
                    Text(
                        text = if (eventsForDay.isNotEmpty()) "\u2713" else "\u2717", // ✓ or ✗
                        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                        color = if (eventsForDay.isNotEmpty()) MaterialTheme.colorScheme.primary else textColor
                    )
                }
            }
        }
    }
}
