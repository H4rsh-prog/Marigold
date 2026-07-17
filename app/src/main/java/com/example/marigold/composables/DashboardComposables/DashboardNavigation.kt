package com.example.marigold.composables.DashboardComposables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
    var currentDestination by remember { mutableStateOf(AppDestinations.entries.indexOf(overrideDestination)) }
    var previousDestination by remember { mutableStateOf(AppDestinations.entries.indexOf(overrideDestination)) }
    var initializedAppDestinations by remember {
        mutableStateOf<List<@Composable (overrideNavigationIndx : (NavigationIndx) -> Unit) -> Unit>>(AppDestinations.entries.map { it.content })
    }
    var launchTimer by remember {mutableStateOf(false)}
    LaunchedEffect(Unit) {
        delay(500)
        launchTimer = true
    }
    NavigationSuiteScaffold(
        {
            AppDestinations.entries.forEachIndexed { index, it ->
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label,
                            modifier = modifier
                                .scale(1.2f)
                                .padding(7.dp),
                            tint = if (index == currentDestination) MaterialTheme.colorScheme.tertiary else Color.Unspecified
                        )
                    },
                    selected = index == currentDestination,
                    onClick = { previousDestination = currentDestination; currentDestination = index; },
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
            AnimatedContent(
                targetState = currentDestination,
                modifier = Modifier.fillMaxSize(),
                transitionSpec = {
                    val animationDuration = 800
                    ContentTransform(
                        targetContentEnter =
                            if(currentDestination>previousDestination){
                                slideInHorizontally(tween(animationDuration)) {it}
                            } else {
                                slideInHorizontally(tween(animationDuration)) {-it}
                            },
                        initialContentExit =
                            if(currentDestination>previousDestination){
                                slideOutHorizontally(tween(animationDuration)) {-it}
                            } else {
                                slideOutHorizontally(tween(animationDuration)) {it}
                            }
                    )
                }
            ) {
                initializedAppDestinations.get(it).invoke(overrideNavigationIndx)
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
    val content: @Composable (overrideNavigationIndx : (NavigationIndx) -> Unit) -> Unit,
) {
    HOME("Home", R.drawable.ic_home, { HomeScreen({ it.invoke(NavigationIndx.NAV_SCREEN) }) }),
    PROFILE("Profile", R.drawable.ic_account_box, { ProfileScreen(overrideNavigationIndx = it) })
}
