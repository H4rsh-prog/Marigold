package com.example.marigold.composables.activity_main

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import com.example.marigold.DashboardActivity

@Composable
fun AppNavigation(viewIndx : Int = 0, onAuthenticate: (() -> Unit) -> Unit = { it() }) {
    var navIndx by remember { mutableStateOf(viewIndx) }
    val context = LocalContext.current;
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        AnimatedVisibility(
            visible = navIndx==0,
            exit = fadeOut(animationSpec = tween(2000)),
            enter = fadeIn(animationSpec = tween(500))
        ) {
            SplashScreen({
                onAuthenticate {
                    navIndx = 1
                }
            })
        }
        AnimatedVisibility(
            visible = navIndx==1,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            HomeScreen({ navIndx = 2 })
        }
        AnimatedVisibility(
            visible = navIndx==2,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            DefineMarigold({
                val intent = Intent(context, DashboardActivity::class.java);
                context.startActivity(intent);
            })
        }
    }
}
