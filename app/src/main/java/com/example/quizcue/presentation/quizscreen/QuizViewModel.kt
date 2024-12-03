package com.example.quizcue.presentation.quizscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.BuildConfig
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.repository.QuestionRepository
import com.example.quizcue.presentation.competition_screen.uiState
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Pair<String, List<Pair<Boolean, String>>>>>(emptyList())
    val uiState: StateFlow<List<Pair<String, List<Pair<Boolean, String>>>>> = _uiState


    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    val courseId: String = savedStateHandle["courseId"] ?: ""

    init {
        createQuiz(courseId)
    }

    private fun generateAnswers(questionText: String, onComplete: (List<Pair<Boolean, String>>) -> Unit) {
        val answers = mutableListOf<Pair<Boolean, String>>()

        viewModelScope.launch {
            val correctAnswer = generateAnswer(isCorrect = true, questionText = questionText)
            correctAnswer?.let { answers.add(true to it) }

            repeat(3) {
                val incorrectAnswer = generateAnswer(isCorrect = false, questionText = questionText)
                incorrectAnswer?.let { answers.add(false to it) }
            }

            onComplete(answers.shuffled())
        }
    }

    private suspend fun generateAnswer(isCorrect: Boolean, questionText: String): String? {
        val prompt = if (isCorrect) {
            "Напиши один правильный ответ на вопрос: \"$questionText\", используя не более 20 слов."
        } else {
            "Напиши один неправильный ответ на вопрос: \"$questionText\", используя не более 20 слов."
        }

        return try {
            val result = generativeModel.generateContent(prompt)
            result.text
        } catch (e: Exception) {
            null
        }
    }

    private fun updateState(questionText: String, answers: List<Pair<Boolean, String>>) {
        _uiState.update { currentList ->
            currentList + (questionText to answers)
        }
    }

    fun createQuiz(courseId: String) {
        viewModelScope.launch {
            questionRepository.getQuestions()
                .map { questionsList -> questionsList.filter { it.course == courseId } }
                .collect { filteredQuestions ->
                    filteredQuestions.shuffled().take(4).forEach { question ->
                        generateAnswers(question.text) { answers ->
                            updateState(question.text, answers)
                        }
                    }
                }
        }
    }

}
