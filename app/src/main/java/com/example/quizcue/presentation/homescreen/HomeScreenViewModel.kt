package com.example.quizcue.presentation.homescreen

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizcue.domain.model.Course
import com.example.quizcue.domain.model.Question
import com.example.quizcue.domain.model.User
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.repository.CompetitionRepository
import com.example.quizcue.domain.repository.CourseRepository
import com.example.quizcue.domain.repository.QuestionRepository
import com.example.quizcue.domain.usecases.registration_UC.LogoutUseCase
import com.example.quizcue.presentation.courses_screen.CourseViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val logoutUseCase: LogoutUseCase,
    private val competitionRepository: CompetitionRepository,
    authenticationRepository: AuthenticationRepository,
    private val courseRepository: CourseRepository,
    auth: FirebaseAuth,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(User())
    val uiState: StateFlow<User> = _uiState

    private val _lastCourse = MutableStateFlow(Course())
    val lastCourse: StateFlow<Course> = _lastCourse

    private val _competitionDate = MutableStateFlow(0L)
    val competitionDate: StateFlow<Long> = _competitionDate

    private val _allQuestionsAmount = MutableStateFlow(0)
    val allQuestionsAmount: StateFlow<Int> = _allQuestionsAmount

    private val _favouriteQuestionsAmount = MutableStateFlow(0)
    val favouriteQuestionsAmount: StateFlow<Int> = _favouriteQuestionsAmount

    val email = auth.currentUser?.email ?: ""

    init {
        authenticationRepository.getUserInfo(auth.currentUser?.uid ?: "") { user ->
            user?.let {
                _uiState.update {
                    it.copy(
                        name = user.name,
                        photo = user.photo,
                        competitionId = user.competitionId
                    )
                }
                competitionRepository.getCompetitionById(user.competitionId) {
                    if (it != null) {
                        _competitionDate.value = it.challengeDate
                    }
                }
            }
        }
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            questionRepository.getQuestions().collect { questions ->
                _allQuestionsAmount.value = questions.size
                _favouriteQuestionsAmount.value = questions.filter { it.isFavourite }.size
            }
        }
    }

    fun logout() = viewModelScope.launch {
        logoutUseCase.invoke()
    }

    fun lastTimeCourse() {
        viewModelScope.launch {
            courseRepository.getCourses().collect { coursesList ->
                coursesList.forEach {
                    if (_lastCourse.value.date < it.date)
                        _lastCourse.value = it
                }
            }
        }
    }

}

