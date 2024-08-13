package com.example.quizcue.presentation.screens.schedule_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CalendarWithEvents(events: List<Event>) {
    val currentMonth = remember { mutableStateOf(Calendar.getInstance()) }

    val selectedDateEvents = remember { mutableStateOf<List<Event>>(listOf()) }

    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2)
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.padding(top = 10.dp)) {
        HorizontalPager(
            count = Int.MAX_VALUE,
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { page ->
            val month = currentMonth.value.clone() as Calendar
            month.add(Calendar.MONTH, page - Int.MAX_VALUE / 2)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 5.dp),
                    text = "${
                        month.getDisplayName(
                            Calendar.MONTH,
                            Calendar.SHORT,
                            Locale.getDefault()
                        )
                    } ${month.get(Calendar.YEAR)}",
                    style = MaterialTheme.typography.bodyLarge,
                )

                CalendarGrid(events, month, selectedDateEvents)
                EventList(selectedDateEvents.value)
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                // Move to the previous page
                coroutineScope.launch {
                    pagerState.scrollToPage(pagerState.currentPage - 1)
                }
            }) {
                Icon(Icons.Filled.ChevronLeft, contentDescription = "Previous Month")
            }
            IconButton(onClick = {
                // Move to the next page
                coroutineScope.launch {
                    pagerState.scrollToPage(
                        pagerState.currentPage + 1,
                    )
                }
            }) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
            }
        }
    }
}


@Composable
fun CalendarGrid(events: List<Event>, currentMonth: Calendar, selectedDateEvents: MutableState<List<Event>>) {
    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfMonth = currentMonth.clone() as Calendar
    firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
    val startingDayOfWeek =
        firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1
    val days = (1..daysInMonth).toList()
    val paddingDaysBefore = List(startingDayOfWeek) { -1 }
    val paddingDaysAfter = List((7 - (startingDayOfWeek + daysInMonth) % 7) % 7) { -1 }
    val allDays = paddingDaysBefore + days + paddingDaysAfter
    val today = Calendar.getInstance()
    val isCurrentMonth = today.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
            today.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR)
    val isSelected  = remember { mutableStateOf(today.get(Calendar.DAY_OF_MONTH)) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(7)
    ) {
        items(allDays.size) { index ->
            val day = allDays[index]
            val textColor = if (day <= 0) Color.Transparent else MaterialTheme.colorScheme.tertiary
            val currentDay = if (day <= 0) "" else day.toString()
            // Determine the events for this day
            val eventsForDay = events.filter { event ->
                val eventCalendar = Calendar.getInstance()
                eventCalendar.time = event.date
                eventCalendar.get(Calendar.DAY_OF_MONTH) == day &&
                        eventCalendar.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH) &&
                        event.date.year == currentMonth.get(Calendar.YEAR)
            }
            val isToday = isCurrentMonth && day == today.get(Calendar.DAY_OF_MONTH)
            // Display the day and event dots
            Surface(
                modifier = Modifier
                    .padding(2.dp)
                    .height(55.dp)
                    .width(50.dp)
                    .clickable {
                        selectedDateEvents.value = eventsForDay
                        isSelected.value = day
                    },
                shape = RoundedCornerShape(15.dp),
                border = if (isToday) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                    else if (isSelected.value == day) BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                    else null
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = currentDay,
                        style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                        color = textColor,
                    )
                    Text(
                        text = if (eventsForDay.isNotEmpty()) "✓" else "✗",
                        style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                        color = if (eventsForDay.isNotEmpty()) MaterialTheme.colorScheme.primary else textColor,
                    )
                }
            }
        }
    }
}

@Composable
fun EventList(events: List<Event>) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        events.forEach { event ->
            Text(text = event.description)
        }
    }
}

