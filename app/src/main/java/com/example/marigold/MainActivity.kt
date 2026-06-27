package com.example.marigold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.marigold.composables.AppNavigation
import com.example.marigold.ui.theme.MarigoldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarigoldTheme() {
                AppNavigation()
            }
        }
    }
}
