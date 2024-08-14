package com.example.quizcue.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.quizcue.presentation.tools.BottomNavGraph
import com.example.quizcue.presentation.tools.BottomNavigationBar
import com.example.quizcue.presentation.tools.currentRouteForBottomBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { if (currentRouteForBottomBar(navController = navController)) {
            BottomNavigationBar(navController)
        }},
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = it.calculateBottomPadding())
            .verticalScroll(rememberScrollState())){}
        BottomNavGraph(navController = navController)
    }
}
