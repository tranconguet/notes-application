package com.andy.notesapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.andy.notesapplication.screens.detail.noteDetailRoute
import com.andy.notesapplication.screens.list.NOTE_LIST_ROUTE
import com.andy.notesapplication.screens.list.noteListRoute
import com.andy.notesapplication.ui.theme.NotesApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesApplicationTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NOTE_LIST_ROUTE
                ) {
                    noteListRoute(navController)
                    noteDetailRoute(navController)
                }
            }
        }
    }
}