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
 /*
    private val _uiState = MutableStateFlow<MutableMap<Boolean, String>>(mutableMapOf())
    val uiState: StateFlow<Map<Boolean, String>> = _uiState

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun generateCorrectAnswer() {
        executeGeneration(
            prompt = "Напиши один неверный ответ к данному вопросу",
            onUpdate = { answer -> updateState {  } }
        )
    }

    fun generateCorrectAnswer() {
        executeGeneration(
            prompt = "Напиши один верный ответ к данному вопросу",
            onUpdate = { answer -> updateState { copy(answer = answer) } }
        )
    }

    private fun executeGeneration(prompt: String, onUpdate: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val result = generativeModel.generateContent(prompt)
                onUpdate(result.text ?: "Не удалось создать")
            } catch (e: Exception) {
                onUpdate("Ошибка генерации")
            }
        }
    }

    private fun updateState(isTrue: Boolean, answer: String) {
        _uiState.update {
            mutableMapOf(isTrue, answer)
        }
    }

  */
}