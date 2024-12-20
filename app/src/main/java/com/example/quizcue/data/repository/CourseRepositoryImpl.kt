package com.example.quizcue.data.repository

import android.util.Log
import com.example.quizcue.domain.model.Course
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.repository.CourseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CourseRepositoryImpl(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : CourseRepository {

    private val databaseRef = database.reference
        .child(auth.currentUser?.uid.toString())

    override fun upsertCourse(course: Course) {
        val courseId = if (course.id == "" ) databaseRef.push().key ?: return else course.id
        val courseMap = hashMapOf<String, Any>(
            "id" to courseId,
            "name" to course.name,
            "description" to course.description,
            "date" to course.date,
        )
        databaseRef.child("courses").child(courseId)
            .setValue(courseMap)
    }

    override fun updateLastTime(courseId: String, date: Long) {
        databaseRef.child("courses").child(courseId).child("date")
            .setValue(date)
    }

    override fun deleteCourse(course: Course) {
        databaseRef.child("courses").child(course.id)
            .removeValue()
    }

    override fun getCoursesProgress(): Flow<Map<String, Float>> = callbackFlow {
        val questionsRef = databaseRef.child("questions")

        val questionListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val progressMap = mutableMapOf<String, Pair<Int, Int>>() // <CourseId, Pair<isStudiedCount, totalCount>>

                snapshot.children.forEach { questionSnapshot ->
                    val courseId = questionSnapshot.child("course").getValue(String::class.java)
                    val isStudied = questionSnapshot.child("isStudied").getValue(Boolean::class.java) ?: false

                    if (courseId != null) {
                        val stats = progressMap[courseId] ?: (0 to 0)
                        progressMap[courseId] = if (isStudied) {
                            stats.copy(first = stats.first + 1, second = stats.second + 1)
                        } else {
                            stats.copy(second = stats.second + 1)
                        }
                    }
                }

                // Вычисляем прогресс
                val progress = progressMap.mapValues { (courseId, stats) ->
                    val (studied, total) = stats
                    if (total > 0) studied.toFloat() / total else 0f
                }
                trySend(progress).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        questionsRef.addValueEventListener(questionListener)
        awaitClose { questionsRef.removeEventListener(questionListener) }
    }



    override fun getCourses(): Flow<List<Course>> = callbackFlow {
        val courseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val courses = mutableListOf<Course>()
                snapshot.child("courses").children.forEach { child ->
                    val id = child.child("id").getValue(String::class.java) ?: ""
                    val name = child.child("name").getValue(String::class.java) ?: ""
                    val description = child.child("description").getValue(String::class.java) ?: ""
                    val date = child.child("date").getValue(Long::class.java) ?: 0L

                    val course = Course(
                        id, name, description, date
                    )
                    courses.add(course)
                }
                trySend(courses).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        databaseRef.addValueEventListener(courseListener)
        awaitClose { databaseRef.removeEventListener(courseListener) }
    }

    override fun getCourseById(courseId: String, onSuccess: (Course?) -> Unit) {
        databaseRef.child("courses").child(courseId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val course = snapshot.getValue(Course::class.java)
                    onSuccess(course)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }


}