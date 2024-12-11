package com.example.quizcue.di

import android.app.Application
import android.content.Context
import com.example.quizcue.data.repository.AuthenticationRepositoryImpl
import com.example.quizcue.data.repository.CalendarRepositoryImpl
import com.example.quizcue.data.repository.CompetitionRepositoryImpl
import com.example.quizcue.data.repository.CourseRepositoryImpl
import com.example.quizcue.data.repository.QuestionRepositoryImpl
import com.example.quizcue.data.repository.QuizRepositoryImpl
import com.example.quizcue.domain.repository.AuthenticationRepository
import com.example.quizcue.domain.repository.CalendarRepository
import com.example.quizcue.domain.repository.CompetitionRepository
import com.example.quizcue.domain.repository.CourseRepository
import com.example.quizcue.domain.repository.QuestionRepository
import com.example.quizcue.domain.repository.QuizRepository
import com.example.quizcue.presentation.tools.PromptProvider
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
import dagger.hilt.android.qualifiers.ApplicationContext

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
        database: FirebaseDatabase,
        auth: FirebaseAuth,
        @ApplicationContext context: Context
    ): AuthenticationRepository = AuthenticationRepositoryImpl(database,auth, context)


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

    @Provides
    @Singleton
    fun provideCourseRepository(
        database: FirebaseDatabase,
        auth: FirebaseAuth
    ): CourseRepository = CourseRepositoryImpl(database, auth)

    @Provides
    @Singleton
    fun provideCompetitionRepository(
        database: FirebaseDatabase,
        auth: FirebaseAuth
    ): CompetitionRepository = CompetitionRepositoryImpl(database, auth)

    @Provides
    @Singleton
    fun provideQuizRepository(
        database: FirebaseDatabase,
        auth: FirebaseAuth
    ): QuizRepository = QuizRepositoryImpl(database, auth)

    @Provides
    @Singleton
    fun provideCalendarRepository(
        database: FirebaseDatabase,
        auth: FirebaseAuth
    ): CalendarRepository = CalendarRepositoryImpl(database, auth)

    @Provides
    fun providePromptProvider(): PromptProvider = PromptProvider()
}