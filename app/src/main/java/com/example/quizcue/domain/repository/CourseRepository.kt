package com.example.quizcue.domain.repository

import com.example.quizcue.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun upsertCourse(course: Course, onSuccess: () -> Unit)
    suspend fun deleteCourse(course: Course, onSuccess: () -> Unit)
    fun getCoursesProgress(): Flow<Map<String, Float>>
    fun getCourses(): Flow<List<Course>>
}