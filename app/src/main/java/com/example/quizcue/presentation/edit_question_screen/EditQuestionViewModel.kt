package com.example.quizcue.presentation.edit_question_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.collectLatest

@HiltViewModel
class EditQuestionViewModel  @Inject constructor(
    private val questionRepository: QuestionRepository,
    savedStateHandler: SavedStateHandle
) : ViewModel() {
    private  val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash" ,
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    private val _questions = mutableStateOf<List<Question>>(emptyList())
    val questions: State<List<Question>> = _questions

    private val _idQuestion = mutableStateOf("")
    val idQuestion: State<String> = _idQuestion

    private val _textQuestion = mutableStateOf("")
    val textQuestion: State<String> = _textQuestion

    private val _answerQuestion = mutableStateOf("")
    val answerQuestion: State<String> = _answerQuestion

    private val _hintQuestion = mutableStateOf("")
    val hintQuestion: State<String> = _hintQuestion

    fun generateAnswer(question: String) {
        _answerQuestion.value = "Generating ..."
        val prompt = "Напиши ответ на вопрос: $question"
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                _answerQuestion.value = result.text ?: "Не удалось создать ответ"
            } catch (e: Exception) {
                _answerQuestion.value = "Не удалось создать ответ"
            }
        }
    }
    fun onEvent(event: EditQuestionEvent) {
        when (event) {
            is EditQuestionEvent.EnteredTextQuestion -> {
                _textQuestion.value = event.value
            }

            is EditQuestionEvent.EnteredHintQuestion -> {
                _hintQuestion.value = event.value
            }

            is EditQuestionEvent.EnteredAnswerQuestion -> {
                _answerQuestion.value = event.value
            }
            is EditQuestionEvent.SaveQuestion -> {
                upsertQuestion(
                    Question(
                        id = idQuestion.value,
                        text = textQuestion.value,
                        hint = hintQuestion.value,
                        answer = answerQuestion.value
                    )) {}
            }
        }
    }
    init {
        getQuestions()
        savedStateHandler.get<String?>("questionId")?.let { questionId ->
            if (questionId != "") {
                viewModelScope.launch {
                    questionRepository.getQuestionById(questionId) { question ->
                        question?.let {
                            _idQuestion.value = it.id
                            _textQuestion.value = it.text
                            _hintQuestion.value = it.hint
                            _answerQuestion.value = it.answer
                        }
                    }
                }
            }
        }
    }
    fun getQuestions() {
        viewModelScope.launch {
            questionRepository.getQuestions().collect{ questionsList ->
                _questions.value = questionsList
            }
        }
    }

    fun generateHint(answer: String) {
        _hintQuestion.value = "Generating ..."
        val prompt = "Напиши подсказку чтобы запомноить этот ответ на вопрос: $answer не более 20 слов"
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                _hintQuestion.value = result.text ?: "Не удалось создать ответ"
            } catch (e: Exception) {
                _hintQuestion.value = "Не удалось создать ответ"
            }
        }
    }

    fun upsertQuestion(question: Question, onSuccess: () -> Unit) = viewModelScope.launch {
            questionRepository.upsertQuestion(question, onSuccess)
            onSuccess()
    }

    fun deleteQuestion(question: Question, onSuccess: () -> Unit) = viewModelScope.launch {
            questionRepository.deleteQuestion(question, onSuccess)
            onSuccess()
    }
}