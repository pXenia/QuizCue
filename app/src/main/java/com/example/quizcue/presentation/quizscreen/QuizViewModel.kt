package com.example.quizcue.presentation.quizscreen

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    questionRepository: QuestionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Pair<Boolean, String>>>(emptyList())
    val uiState: StateFlow<List<Pair<Boolean, String>>> = _uiState

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun onEvent (event: QuizEvent){
        when(event){
            is QuizEvent.GenerateCorrectAnswer -> generateAnswer(isCorrect = true, questionText = event.questionText)
            is QuizEvent.GenerateIncorrectAnswer -> generateAnswer(isCorrect = false, questionText = event.questionText)
            is QuizEvent.ClearAnswers -> clearAnswer()
        }
    }

    private  fun generateAnswer(isCorrect: Boolean, questionText: String){
        val prompt = if (isCorrect) {
            "Напиши один правильный ответ на вопрос: \"$questionText\"."
        } else {
            "Напиши один неправильный ответ на вопрос: \"$questionText\"."
        }

        executeGeneration(prompt) { answer ->
            updateState(isCorrect, answer)
        }
    }

    private fun executeGeneration(prompt: String, onUpdate: (String) -> Unit){
        viewModelScope.launch {
            try {
                val result = generativeModel.generateContent(prompt)
                onUpdate (result.text ?: "Не удалось создать")
            } catch ( e: Exception) {
                onUpdate("Ошибка генерации")
            }
        }
    }

    private fun updateState(isCorrect: Boolean, answer: String){
        _uiState.update {currentList ->
            currentList+ (isCorrect to answer)

        }
    }

    private fun clearAnswer(){
        _uiState.update { emptyList() }
    }
}