package com.example.marigold.composables.DashboardComposables

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.marigold.R
import com.example.marigold.composables.NavigationIndx

data object profile

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, overrideNavigationIndx : (NavigationIndx) -> Unit){
    val backStack = remember { mutableStateListOf<Any>(profile) }
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
                    Box(
                        modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondary).padding(40.dp)
                            .border(2.dp, MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Spacer(modifier.height(60.dp))
                        Icon(painter = painterResource(R.drawable.lamare_della_mi_vita_trasparent), contentDescription = null)
                    }
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item { Spacer(modifier.height(750.dp)) }
                        item {
                            Box(
                                modifier = modifier
                                    .width(300.dp)
                                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(50.dp,50.dp,0.dp,0.dp))
                                    .padding(0.dp,20.dp,0.dp,0.dp)
                            ) {
                                Text(text = "Welcome Marigold", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium, modifier = modifier.fillMaxSize())
                            }
                        }
                        item {
                            Column(
                                modifier.fillParentMaxHeight(1f).padding(15.dp, 0.dp, 15.dp, 0.dp).background(MaterialTheme.colorScheme.background, RoundedCornerShape(50.dp,50.dp)).padding(40.dp)
                            ){
                                Spacer(modifier.height(50.dp))
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
                                            backStack.add(item);
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