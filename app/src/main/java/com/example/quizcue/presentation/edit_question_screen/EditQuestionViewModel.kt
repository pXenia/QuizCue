package com.example.quizcue.presentation.edit_question_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizcue.BuildConfig
import com.example.quizcue.domain.Response
import com.example.quizcue.domain.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.quizcue.domain.model.Question
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class EditQuestionViewModel  @Inject constructor(
    private val questionRepository: QuestionRepository
) : ViewModel() {
    private  val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash" ,
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    private val _questionsFlow = MutableStateFlow<Response<List<Question>>>(Response.Loading)
    val questionsFlow: StateFlow<Response<List<Question>>> = _questionsFlow

    private val _answerGenerationResult = MutableStateFlow<String?>(null)
    val answerGenerationResult = _answerGenerationResult.asStateFlow()

    private val _hintGenerationResult = MutableStateFlow<String?>(null)
    val hintGenerationResult = _hintGenerationResult.asStateFlow()

    fun generateAnswer(question: String) {
        _answerGenerationResult.value = "Generating ..."
        val prompt = "Напиши ответ на вопрос: $question"
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                _answerGenerationResult.value = result.text
            } catch (e: Exception) {
                _answerGenerationResult.value = "Error: ${e.message}"
            }
        }
    }
    fun generateHint(answer: String) {
        _hintGenerationResult.value = "Generating ..."
        val prompt = "Напиши подсказку чтобы запомноить этот ответ на вопрос: $answer не более 20 слов"
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                _hintGenerationResult.value = result.text
            } catch (e: Exception) {
                _hintGenerationResult.value = "Error: ${e.message}"
            }
        }
    }

    fun addQuestion(question: Question) = viewModelScope.launch {
        val response = questionRepository.addQuestion(question)
    }

}