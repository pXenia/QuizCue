package com.example.quizcue.presentation.questionslearncreens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.repository.CalendarRepository
import com.example.quizcue.domain.repository.CourseRepository
import com.example.quizcue.domain.repository.QuestionRepository
import com.example.quizcue.domain.repository.QuizRepository
import com.google.android.play.integrity.internal.c
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class QuestionsScreensViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val courseRepository: CourseRepository,
    private val calendarRepository: CalendarRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    val courseId: String = savedStateHandle["courseId"] ?: ""

    private val _courseTitle = mutableStateOf("")
    val courseTitle: State<String> = _courseTitle

    init {
        _courseTitle.value = when (courseId) {
            "all" -> "Все вопросы"
            "favourite" -> "Избранное"
            else -> ""
        }

        when (courseId) {
            "all" -> loadQuestions { questionRepository.getQuestions() }
            "favourite" -> loadQuestions {
                questionRepository.getQuestions().map { it.filter { question -> question.isFavourite } }
            }
            "" -> Unit
            else -> {
                loadQuestions {
                    questionRepository.getQuestions().map { it.filter { question -> question.course == courseId } }
                }
                loadCourseTitle()
            }
        }
    }

    private fun loadQuestions(fetcher: suspend () -> Flow<List<Question>>) {
        viewModelScope.launch {
            fetcher().collect { filteredQuestions ->
                _questions.value = filteredQuestions
            }
        }
    }

    private fun loadCourseTitle() {
        viewModelScope.launch {
            courseRepository.getCourseById(courseId) { course ->
                course?.let {
                    _courseTitle.value = it.name
                }
            }
        }
    }

    fun updateQuestionIsStudied(question: Question, isStudied: Boolean){
        viewModelScope.launch {
            questionRepository.upsertQuestion(
                question.copy(isStudied = isStudied)
            ){}
        }

    }

    fun addToFavorites(questionId: String, isFavourite: Boolean) {
        questionRepository.addFavourite(questionId,isFavourite)
    }

    fun deleteQuestion(question: Question) {
        viewModelScope.launch {
            questionRepository.deleteQuestion(question)
        }
    }

    fun updateLastTime(){
        courseRepository.updateLastTime(
            courseId = courseId,
            date = System.currentTimeMillis()
        )
    }

    fun updateStat(){
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        calendarRepository.updateRepetitionNumber(calendar.timeInMillis)
    }

}