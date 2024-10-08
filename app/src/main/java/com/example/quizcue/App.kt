package com.example.quizcue

import android.app.Application
import com.example.quizcue.data.repository.AuthenticationRepositoryImpl
import com.example.quizcue.data.repository.QuestionRepositoryImpl
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.repository.QuestionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class App : Application()

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        auth: FirebaseAuth
    ): AuthenticationRepository = AuthenticationRepositoryImpl(auth)

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideQuestionRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): QuestionRepository = QuestionRepositoryImpl(firestore, auth)
}