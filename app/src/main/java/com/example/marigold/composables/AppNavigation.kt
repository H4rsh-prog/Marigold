package com.example.marigold.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import com.example.marigold.composables.DashboardComposables.HomeScreen
import com.example.marigold.composables.DashboardComposables.ProfileTabs
import com.example.marigold.composables.PreAuthComposables.DefineMarigold
import com.example.marigold.composables.PreAuthComposables.SplashScreen
import com.example.marigold.services.DataHandler

enum class NavigationIndx (val index : Int){
    SPLASH_SCREEN(-1),
    AUTH_SCREEN(0),
    NAV_SCREEN(1)
}
@Composable
fun AppNavigation(
    overrideNavIndx: Int = NavigationIndx.SPLASH_SCREEN.index,
    overrideProfileTabs: ProfileTabs? = null,
    onAuthenticate: (() -> Unit) -> Unit = { it() },
    dataHandler: DataHandler = DataHandler(LocalContext.current)
) {
    var navIndx by remember { mutableStateOf(overrideNavIndx) }
    var prevNavIndx by remember {mutableStateOf(overrideNavIndx)}
    val overrideNavigationIndx : (NavigationIndx) -> Unit = { destination -> navIndx = destination.index }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        AnimatedContent(
            targetState = navIndx,
            modifier = Modifier.fillMaxSize(),
            label = "navigation",
            transitionSpec = {
                val animationDuration = 800
                ContentTransform(
                    targetContentEnter =
                        if(targetState>prevNavIndx) {
                            slideInVertically(animationSpec = tween(animationDuration)) { it * 2 }
                        } else {
                            scaleIn(animationSpec = tween(animationDuration))
                        },
                    initialContentExit =
                        if(targetState>prevNavIndx) {
                            scaleOut(tween(animationDuration), targetScale = 2.5F, )
                        } else {
                            slideOutVertically(tween(animationDuration)) { it*2 }
                        }
                )
            }
        ) {
            prevNavIndx = it
            when (it) {
                -1 ->
                    SplashScreen(
                        resolveView = { onAuthenticate { navIndx = NavigationIndx.AUTH_SCREEN.index; } }
                    )
                0 ->
                    DefineMarigold(
                        resolveView = { navIndx = NavigationIndx.NAV_SCREEN.index; },
                        isInitialized = dataHandler.isAppInitialized()
                    )
                1 ->
                    Box(modifier = Modifier.fillMaxSize().background(brush = Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary)), alpha = 0.6f)){
                        HomeScreen(
                            overrideNavigationIndx = overrideNavigationIndx,
                            overrideProfileTabs = overrideProfileTabs
                        )
                    }
            }
        }
    }
}
