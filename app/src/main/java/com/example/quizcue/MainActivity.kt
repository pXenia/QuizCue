package com.example.quizcue

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.quizcue.presentation.MainScreen
import com.example.quizcue.presentation.edit_question_screen.EditQuestion
import com.example.quizcue.ui.theme.QuizCueTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizCueTheme {
                MainScreen()
            }
        }
    }
}
