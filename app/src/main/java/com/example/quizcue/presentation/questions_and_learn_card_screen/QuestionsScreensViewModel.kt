package com.example.quizcue.presentation.questions_and_learn_card_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.domain.model.Question
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
    savedStateHandler: SavedStateHandle
): ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    val courseQuestion: String = savedStateHandler["courseId"] ?: ""

    init {
        getQuestions()
    }

    private fun getQuestions() {
        viewModelScope.launch {
            questionRepository.getQuestions()
                .map { questionsList -> questionsList.filter { it.course == courseQuestion } }
                .collect { filteredQuestions ->
                    _questions.value = filteredQuestions
                }
        }
    }
}