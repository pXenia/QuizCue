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
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<QuizUIState>>(emptyList())
    val uiState: StateFlow<List<QuizUIState>> = _uiState

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    val courseId: String = savedStateHandle["courseId"] ?: ""

    init {
        createQuiz(courseId)
    }

    private fun generateAnswers(
        questionText: String,
        onComplete: (QuizUIState) -> Unit
    ) {
        viewModelScope.launch {
            val correctAnswer = generateAnswer(isCorrect = true, questionText = questionText)
            val incorrectAnswers = mutableListOf<String>()

            repeat(3) {
                val incorrectAnswer = generateAnswer(isCorrect = false, questionText = questionText)
                incorrectAnswer?.let { incorrectAnswers.add(it) }
            }

            if (correctAnswer != null) {
                val allAnswers = (incorrectAnswers + correctAnswer).shuffled()
                val uiState = QuizUIState(
                    questionText = questionText,
                    answers = allAnswers,
                    correctAnswer = correctAnswer
                )
                onComplete(uiState)
            }
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

    private fun updateState(quizState: QuizUIState) {
        _uiState.update { currentList ->
            currentList + quizState
        }
    }

    fun createQuiz(courseId: String) {
        viewModelScope.launch {
            questionRepository.getQuestions()
                .map { questionsList -> questionsList.filter { it.course == courseId } }
                .collect { filteredQuestions ->
                    filteredQuestions.shuffled().take(4).forEach { question ->
                        generateAnswers(question.text) { quizState ->
                            updateState(quizState)
                        }
                    }
                }
        }
    }

    fun processAnswer(questionText: String, selectedAnswer: String) {
        _uiState.update { currentList ->
            currentList.map { quizState ->
                if (quizState.questionText == questionText) {
                    quizState.copy(
                        selectedAnswer = selectedAnswer,
                        isCorrect = quizState.correctAnswer == selectedAnswer
                    )
                } else quizState
            }
        }
    }

    fun scoreCalculate() {
        uiState.value.forEach { quizUiState ->
            quizUiState.isCorrect?.let {
                if (quizUiState.isCorrect)
                    incrementScore()
            }

        }
    }

    private fun incrementScore() {
        _score.update { it + 1 }
    }
}



data class QuizUIState(
    val questionText: String,
    val answers: List<String>,
    val correctAnswer: String,
    val selectedAnswer: String? = null,
    val isCorrect: Boolean? = null
)

