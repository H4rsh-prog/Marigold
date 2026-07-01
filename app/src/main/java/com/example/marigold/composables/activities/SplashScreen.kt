package com.example.marigold.composables.activities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.marigold.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(homeView: () -> Unit = {}, modifier : Modifier = Modifier) {
    var splashed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(1000)
        splashed = true
    }
    LaunchedEffect(Unit) {
        delay(7000)
        homeView()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable(enabled = true, onClick = {homeView()}),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Marigold Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier.size(120.dp)
            )
            Spacer(modifier = modifier.height(16.dp))
            Text(
                text = "Marigold",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = modifier.height(2.dp))
            Text(
                text = "some people are like flowers",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = modifier.fillMaxSize()
                .padding(0.dp, 0.dp, 0.dp,70.dp)
        )  {
            AnimatedVisibility(
                visible = splashed,
                enter = fadeIn(animationSpec = tween(2000)),
                modifier = modifier
            ) {
                Text(
                    text = "Press anywhere to continue",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}
