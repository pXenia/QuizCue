package com.example.quizcue.di

import android.app.Application
import com.example.quizcue.data.repository.AuthenticationRepositoryImpl
import com.example.quizcue.data.repository.QuestionRepositoryImpl
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.repository.QuestionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.google.firebase.ktx.Firebase

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
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseDatabaseModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = Firebase.database("https://quizcue-298f3-default-rtdb.europe-west1.firebasedatabase.app")

    @Provides
    @Singleton
    fun provideQuestionRepository(
        database: FirebaseDatabase,
        auth: FirebaseAuth
    ): QuestionRepository = QuestionRepositoryImpl(database, auth)
}