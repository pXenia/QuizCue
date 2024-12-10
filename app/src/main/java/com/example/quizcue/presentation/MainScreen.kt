package com.example.quizcue.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.quizcue.presentation.tools.AppNavGraph
import com.example.quizcue.presentation.tools.BottomNavigationBar
import com.example.quizcue.presentation.tools.currentRouteForBottomBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController){
    Scaffold(
        bottomBar = { if (currentRouteForBottomBar(navController = navController)) {
            BottomNavigationBar(navController)
        }},
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = it.calculateBottomPadding())){}
        AppNavGraph(navController = navController)
    }
}
