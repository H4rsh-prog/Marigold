@file:OptIn(ExperimentalAnimationApi::class)

package com.example.marigold.composables

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.marigold.DashboardActivity
import com.example.marigold.composables.activity_main.DefineMarigold
import com.example.marigold.composables.activity_main.SplashScreen
import com.example.marigold.services.DataHandler
import kotlinx.coroutines.delay

@Composable
fun AppNavigation(
    viewIndx : Int = 0,
    onAuthenticate: (() -> Unit) -> Unit = { it() },
    dataHandler: DataHandler = DataHandler(LocalContext.current)
) {
    var navIndx by remember { mutableStateOf(viewIndx) }
    val context = LocalContext.current;
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        //SPLASH SCREEN
        AnimatedVisibility(
            visible = navIndx==0,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            SplashScreen({
                onAuthenticate {
                    navIndx = -1;
                }
            })
        }
        //AUTH SCREEN
        AnimatedVisibility(
            visible = navIndx==-1,
            enter = fadeIn(animationSpec = tween(100)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            if(dataHandler.isAppInitialized()) {
                DefineMarigold({
                    navIndx = 1;
                }, isInitialized = true)
            } else {
                DefineMarigold({
                    navIndx = 0;
                }, isInitialized = false)
            }
        }
        //HOME SCREEN
        AnimatedVisibility(
            visible = navIndx==1,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            LaunchedEffect(Unit) {
                delay(400)
                context.startActivity(Intent(context, DashboardActivity::class.java));
                delay(1000)
                navIndx = 0;
            }
        }
    }
}
