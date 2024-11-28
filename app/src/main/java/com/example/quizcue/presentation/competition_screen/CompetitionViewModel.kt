package com.example.quizcue.presentation.competition_screen

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CompetitionViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val auth: FirebaseAuth,
    ): ViewModel() {
    //val userName: Flow<String> = authenticationRepository.getUserName(auth.currentUser?.uid ?: "")
    //val userPhoto: Flow<Bitmap?> = authenticationRepository.getUserPhoto(auth.currentUser?.uid ?: "")
    //val opponentName: Flow<String> = authenticationRepository.getUserName("optJ8HMmocTOPPaYG257BzgdAEm2")
    //val opponentPhoto: Flow<Bitmap?> = authenticationRepository.getUserPhoto("optJ8HMmocTOPPaYG257BzgdAEm2")
}