package com.example.quizcue.domain.repository

import com.example.quizcue.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun upsertCourse(course: Course, onSuccess: () -> Unit)
    suspend fun deleteCourse(course: Course, onSuccess: () -> Unit)
    suspend fun getCourseByID(courseId: String, onSuccess: (Course?) -> Unit)
    fun getCourses(): Flow<List<Course>>
}