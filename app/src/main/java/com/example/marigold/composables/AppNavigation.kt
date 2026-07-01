package com.example.marigold.composables

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
import com.example.marigold.composables.activities.DefineMarigold
import com.example.marigold.composables.activities.HomeScreen
import com.example.marigold.composables.activities.SplashScreen

@Composable
public fun AppNavigation() {
    var navIndx by remember { mutableStateOf(0) }
    val context = LocalContext.current;
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        AnimatedVisibility(
            visible = navIndx==0,
            exit = fadeOut(animationSpec = tween(2000)),
            enter = fadeIn(animationSpec = tween(500))
        ) {
            SplashScreen({ navIndx = 1 })
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
