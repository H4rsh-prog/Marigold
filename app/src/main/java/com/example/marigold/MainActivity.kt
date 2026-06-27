package com.example.marigold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.marigold.composables.*
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

@Preview(showBackground = true)
@Composable
fun PreviewDark() {
    MarigoldTheme(darkTheme = true) {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLight() {
    MarigoldTheme(darkTheme = false) {
        HomeScreen();
    }
}
