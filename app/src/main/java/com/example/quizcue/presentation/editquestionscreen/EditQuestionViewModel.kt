package com.example.quizcue.presentation.editquestionscreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.BuildConfig
import com.example.quizcue.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.quizcue.domain.model.Question
import com.example.quizcue.presentation.tools.PromptProvider
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.update

@HiltViewModel
class EditQuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val promptProvider: PromptProvider,
    savedStateHandler: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(Question())
    val uiState: StateFlow<Question> = _uiState

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    init {
        val questionId = savedStateHandler["questionId"] ?: ""
        val courseId = savedStateHandler["courseId"] ?: ""
        _uiState.update { it.copy(course = courseId) }

        if (questionId.isNotEmpty()) {
            viewModelScope.launch {
                questionRepository.getQuestionById(questionId) { question ->
                    question?.let {
                        _uiState.update {
                            it.copy(
                                id = question.id,
                                text = question.text,
                                hint = question.hint,
                                answer = question.answer
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: EditQuestionEvent) {
        when (event) {
            is EditQuestionEvent.EnteredTextQuestion -> updateState { copy(text = event.value) }
            is EditQuestionEvent.EnteredHintQuestion -> updateState { copy(hint = event.value) }
            is EditQuestionEvent.EnteredAnswerQuestion -> updateState { copy(answer = event.value) }
            is EditQuestionEvent.SaveQuestion -> saveQuestion()
        }
    }

    private fun updateState(update: Question.() -> Question) {
        _uiState.update(update)
    }

    private fun saveQuestion() {
        viewModelScope.launch {
            val upsertQuestion = _uiState.value
            questionRepository.upsertQuestion(question = upsertQuestion)
        }
    }

    fun generateAnswer() {
        updateState { copy(answer = "Generating ...") }
        executeGeneration(
            prompt = promptProvider.generateAnswerPrompt(uiState.value.text),
            onUpdate = { answer -> updateState { copy(answer = answer) } }
        )
    }

    fun generateHint() {
        updateState { copy(hint = "Generating ...") }
        executeGeneration(
            prompt = promptProvider.generateHintPrompt(uiState.value.answer),
            onUpdate = { hint -> updateState { copy(hint = hint) } }
        )
    }

    private fun executeGeneration(prompt: String, onUpdate: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val result = generativeModel.generateContent(prompt)
                onUpdate(result.text ?: promptProvider.generationFailed())
            } catch (e: Exception) {
                onUpdate(promptProvider.generationError())
            }
        }
    }
}
