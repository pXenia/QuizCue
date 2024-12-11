package com.example.quizcue.presentation.coursesscreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.model.Course
import com.example.quizcue.domain.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
): ViewModel() {

    private val _courses = mutableStateOf<List<Course>>(emptyList())
    val courses: State<List<Course>> = _courses

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _progress = mutableStateOf<Map<String, Float>>(emptyMap())
    val progress: State<Map<String, Float>> = _progress

    init {
        getCourses()
        getCoursesProgress()
    }

    private fun getCourses() {
        viewModelScope.launch {
            courseRepository.getCourses().collect{ coursesList ->
                _courses.value = coursesList
            }
        }
    }

    private fun getCoursesProgress() {
        viewModelScope.launch {
            courseRepository.getCoursesProgress().collect{ coursesProgress ->
                _progress.value = coursesProgress
            }
        }
    }

    fun onEvent(event: EditCourseEvent) {
        when (event) {
            is EditCourseEvent.EnteredTextCourse -> {
                _name.value = event.value
            }

            is EditCourseEvent.EnteredDescriptionCourse -> {
                _description.value = event.value
            }

            is EditCourseEvent.SaveCourse -> {
                upsertCourse(
                    Course(
                        id = "",
                        name = name.value,
                        description = description.value
                    )
                )
            }
        }
    }

    private fun upsertCourse(course: Course){
        courseRepository.upsertCourse(course)
    }


    fun deleteCourse(course: Course){
        courseRepository.deleteCourse(course)
    }

}