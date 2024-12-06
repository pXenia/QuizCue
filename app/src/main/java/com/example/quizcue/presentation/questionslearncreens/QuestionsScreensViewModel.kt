package com.example.quizcue.presentation.questionslearncreens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.repository.CourseRepository
import com.example.quizcue.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsScreensViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val courseRepository: CourseRepository,
    savedStateHandler: SavedStateHandle
): ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    val courseId: String = savedStateHandler["courseId"] ?: ""

    private var _courseTitle = mutableStateOf("")
    val courseTitle: State<String> = _courseTitle

    init {
        getQuestions()
        if (courseId != ""){
            viewModelScope.launch {
                courseRepository.getCourseById(courseId) { course ->
                    course?.let {
                        _courseTitle.value = it.name
                    }
                }
            }
        }
    }

    private fun getQuestions() {
        viewModelScope.launch {
            questionRepository.getQuestions()
                .map { questionsList -> questionsList.filter { it.course == courseId } }
                .collect { filteredQuestions ->
                    _questions.value = filteredQuestions
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

    fun addToFavorites(question: Question) {

    }

    fun deleteQuestion(question: Question) {
        viewModelScope.launch {
            questionRepository.deleteQuestion(question)
        }
    }



}