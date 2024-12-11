package com.example.quizcue.presentation.schedule_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.model.CalendarData
import com.example.quizcue.domain.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository,
): ViewModel() {

     private val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val startFilterDate = calendar.timeInMillis - 7 * 24 * 60 * 60 * 1000

    private val _calendarData = MutableStateFlow<List<CalendarData>>(emptyList())
    val calendarData: StateFlow<List<CalendarData>> = _calendarData

    private val _calendarDataLastWeek = MutableStateFlow<List<CalendarData>>(emptyList())
    val calendarDataLastWeek: StateFlow<List<CalendarData>> = _calendarDataLastWeek

    init {
        viewModelScope.launch {
            calendarRepository.getData().collect {
                _calendarData.value = it
                _calendarDataLastWeek.value = it.filter {
                    it.date > startFilterDate
                }
            }
        }
    }
}