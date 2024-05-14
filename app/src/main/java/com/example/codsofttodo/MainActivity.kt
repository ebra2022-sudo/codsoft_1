package com.example.codsofttodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.codsofttodo.ui.theme.CodSoftTodoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodSoftTodoTheme(
                darkTheme = true) {
                ScreenContainer()
            }
        }
    }
}


