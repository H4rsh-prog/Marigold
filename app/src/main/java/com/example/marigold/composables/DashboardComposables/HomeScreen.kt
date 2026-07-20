package com.example.marigold.composables.DashboardComposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.marigold.R
import com.example.marigold.composables.NavigationIndx
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data object profile

@Composable
fun HomeScreen(modifier: Modifier = Modifier, overrideNavigationIndx : (NavigationIndx) -> Unit){
    val backStack = remember { mutableStateListOf<Any>(profile) }
    val scope = rememberCoroutineScope();
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull(); if(backStack.isNullOrEmpty()) {overrideNavigationIndx(NavigationIndx.SPLASH_SCREEN)} },
        transitionSpec = {
            val animationDuration = 800
            ContentTransform(
                targetContentEnter = slideInHorizontally(tween(animationDuration)) { it },
                initialContentExit = slideOutHorizontally(tween(animationDuration)) { -it }
            )
        },
        entryProvider = { key ->
            when(key) {
                is profile -> NavEntry(key) {
                    AnimatedVisibility(
                        visible = key is profile,
                        enter = fadeIn(animationSpec = tween(1000)),
                        exit = fadeOut(animationSpec = tween(1000))
                    ) {
                        var showBackdrop by remember { mutableStateOf(false) }
                        var showProfileTabs by remember { mutableStateOf(false) }
                        var showRedefine by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) {
                            delay(1000)
                            showBackdrop = true
                            delay(1000)
                            showProfileTabs = true
                        }
                        Box(
                            modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.secondary).padding(40.dp)
                                .border(2.dp, MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            AnimatedVisibility(
                                visible = showBackdrop,
                                enter = slideInVertically(animationSpec = tween(1000)){-it},
                                exit = slideOutVertically(animationSpec = tween(1000)){-it}
                            ) {
                                Column (modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(painter = painterResource(R.drawable.lamare_della_mi_vita_trasparent), contentDescription = null)
                                    Text(text = "Welcome Marigold", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium, modifier = modifier.fillMaxSize())
                                }
                            }
                        }
                        AnimatedVisibility(
                            visible = showProfileTabs,
                            enter = slideInVertically(animationSpec = tween(1000)) {it},
                            exit = slideOutVertically(animationSpec = tween(1000)) {it}
                        ) {
                            LazyColumn(
                                modifier = modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                item { Spacer(modifier.height(750.dp)) }
                                item {
                                    Column(
                                        modifier = modifier
                                            .fillParentMaxWidth(0.7f)
                                            .height(40.dp)
                                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(50.dp,50.dp,0.dp,0.dp))
                                            .padding(20.dp,20.dp,20.dp,0.dp)
                                    ) {
                                        Box(modifier = modifier
                                            .fillMaxSize()
                                            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(50.dp,50.dp,10.dp,10.dp))
                                            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50.dp,50.dp,10.dp,10.dp))
                                        )
                                    }
                                }
                                item {
                                    Column(
                                        modifier
                                            .fillParentMaxHeight(1f)
                                            .fillMaxWidth(0.9f)
                                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(50.dp,50.dp))
                                            .padding(10.dp,10.dp,10.dp,0.dp)
                                    ) {
                                        Column(
                                            modifier
                                                .fillMaxSize()
                                                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(50.dp,50.dp))
                                                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50.dp,50.dp))
                                                .padding(10.dp,10.dp,10.dp,0.dp)
                                        ) {
                                            Column(
                                                modifier
                                                    .fillMaxSize()
                                                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(50.dp,50.dp))
                                                    .padding(40.dp)
                                            ) {
                                                Spacer(modifier.height(20.dp))
                                                ProfileTabs.entries.forEach { item ->
                                                    Card(
                                                        shape = RoundedCornerShape(
                                                            topStart = 24.dp,
                                                            topEnd = 4.dp,
                                                            bottomStart = 4.dp,
                                                            bottomEnd = 24.dp
                                                        ),
                                                        elevation = CardDefaults.elevatedCardElevation(
                                                            defaultElevation = 4.dp
                                                        ),
                                                        border = BorderStroke(
                                                            1.5.dp,
                                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                                        ),
                                                        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(20.dp),
                                                        onClick = {
                                                            scope.launch {
                                                                showProfileTabs = false
                                                                delay(700)
                                                                backStack.add(item)
                                                            }
                                                        }
                                                    ) {
                                                        Text(
                                                            text = item.label,
                                                            style = MaterialTheme.typography.titleLarge,
                                                            textAlign = TextAlign.Center,
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(18.dp)
                                                        )
                                                    }
                                                }
                                                RedefineAuth({overrideNavigationIndx(NavigationIndx.AUTH_SCREEN)})
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                is ProfileTabs -> NavEntry(key) {
                    key.content.invoke({})
                }
                else -> NavEntry(Unit) { Text("Unknown route") }
            }
        }
    )
}