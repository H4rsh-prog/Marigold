package com.example.marigold.composables.activity_dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults.colors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.marigold.R
import com.example.marigold.composables.NavigationIndx
import kotlinx.coroutines.delay

@Composable
fun Dashboard(overrideDestination: AppDestinations = AppDestinations.HOME, modifier : Modifier = Modifier, overrideNavigationIndx : (NavigationIndx) -> Unit) {
    var currentDestination by rememberSaveable { mutableStateOf(overrideDestination) }
    var launchTimer by remember {mutableStateOf(false)}
    LaunchedEffect(Unit) {
        delay(500)
        launchTimer = true
    }
    NavigationSuiteScaffold(
        {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label,
                            modifier = modifier
                                .scale(1.2f)
                                .padding(7.dp),
                            tint = if (it == currentDestination) MaterialTheme.colorScheme.tertiary else Color.Unspecified
                        )
                    },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it },
                    modifier = modifier.alpha(0.6f)
                )
            }
        },
        layoutType = NavigationSuiteType.NavigationBar,
        navigationSuiteColors = colors(
            navigationBarContainerColor = MaterialTheme.colorScheme.onBackground
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = launchTimer,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                currentDestination.content()
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
    val content: @Composable () -> Unit
) {
    HOME("Home", R.drawable.ic_home, { DashboardHome()}),
    PROFILE("Profile", R.drawable.ic_account_box, { DashboardProfile() })
}
