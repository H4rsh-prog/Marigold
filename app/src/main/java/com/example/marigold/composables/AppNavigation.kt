package com.example.marigold.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
public fun AppNavigation() {
    var showSplash by remember { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        AnimatedVisibility(
            visible = showSplash,
            exit = fadeOut(animationSpec = tween(500)),
            enter = fadeIn(animationSpec = tween(500))
        ) {
            SplashScreen({showSplash = false})
        }

        AnimatedVisibility(
            visible = !showSplash,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            HomeScreen({showSplash = true})
        }
    }
}
