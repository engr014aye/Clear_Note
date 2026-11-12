package com.umerini.clearnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.umerini.clearnote.ui.navigation.ClearNoteNavHost
import com.umerini.clearnote.ui.theme.ClearNoteTheme
import com.umerini.clearnote.ui.theme.DeepSlate900

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as ClearNoteApplication
        val repository = app.noteRepository

        setContent {
            ClearNoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DeepSlate900
                ) {
                    val navController = rememberNavController()
                    ClearNoteNavHost(
                        navController = navController,
                        repository = repository
                    )
                }
            }
        }
    }
}
