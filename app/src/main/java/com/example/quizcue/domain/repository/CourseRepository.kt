package com.example.quizcue.domain.repository

import com.example.quizcue.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun upsertCourse(course: Course, onSuccess: () -> Unit)
    fun deleteCourse(course: Course, onSuccess: () -> Unit)
    fun getCourseById(courseId: String, onSuccess: (Course?) -> Unit)
    fun updateLastTime(courseId: String, date: Long)
    fun getCoursesProgress(): Flow<Map<String, Float>>
    fun getCourses(): Flow<List<Course>>
}