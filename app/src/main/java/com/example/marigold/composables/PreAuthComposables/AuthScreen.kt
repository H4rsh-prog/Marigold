package com.example.marigold.composables.PreAuthComposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.marigold.BuildConfig
import com.example.marigold.services.DataHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DefineMarigold(
    resolveView : () -> Unit = {},
    modifier : Modifier = Modifier,
    dataHandler : DataHandler = DataHandler(LocalContext.current),
    isInitialized : Boolean
){
    var input by remember { mutableStateOf("") }
    var DEFINE_MARIGOLD by remember { mutableStateOf(dataHandler.getPreference(dataHandler.DEFINE_MARIGOLD)) }
    val scope = rememberCoroutineScope()
    val focus = LocalFocusManager.current;
    var loaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(3000)
        loaded = true
    }
    Box (
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(state = rememberScrollState(), enabled = true),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if(isInitialized) {
                Card(
                    modifier = modifier
                        .width(300.dp)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    )
                ) {
                    TextField(
                        value = input,
                        onValueChange = { text ->
                            input = text;
                            if (input.equals(DEFINE_MARIGOLD)) {
                                scope.launch {
                                    focus.clearFocus(true)
                                    delay(500)
                                    resolveView()
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(focusedTextColor = MaterialTheme.colorScheme.secondary),
                        label = {
                            Text(
                                text = "What is Marigold to you?",
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        modifier = modifier.fillMaxSize()
                    )
                }
                AnimatedVisibility(
                    visible = DEFINE_MARIGOLD != BuildConfig.defineMarigold && loaded,
                    enter = fadeIn(animationSpec = tween(1000)),
                    exit = fadeOut(animationSpec = tween(1000))
                ) {
                    Text(text = "forgot your marigold? try mine..", color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.clickable(enabled = true, onClick = {
                        dataHandler.savePreference(dataHandler.DEFINE_MARIGOLD, BuildConfig.defineMarigold)
                        DEFINE_MARIGOLD = dataHandler.getPreference(dataHandler.DEFINE_MARIGOLD)
                    }).scale(0.7f))
                }
            } else {
                Text(
                    text = "Marigold can mean a lot of things,\nfor some it is a symbol of remembrance,\nfor some it is grief,\nand for some it is a gift.\nBut what is it to you?",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = modifier.padding(16.dp)
                )
                Spacer(modifier=modifier.height(40.dp))
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    )
                ) {
                    TextField(
                        value = input,
                        onValueChange = { text ->
                            run {
                                input = text;
                            }
                        },
                        colors = TextFieldDefaults.colors(focusedTextColor = MaterialTheme.colorScheme.secondary),
                        label = {
                            Text(
                                text = "Define Marigold",
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        modifier = modifier.fillMaxSize()
                    )
                }
                Spacer(modifier=modifier.height(20.dp))
                Button(onClick = {
                    if(input.isNotBlank()) {
                        scope.launch {
                            focus.clearFocus(true)
                            delay(500)
                            resolveView()
                            delay(500)
                            dataHandler.savePreference(dataHandler.DEFINE_MARIGOLD, input);
                        }
                    }
                },
                    modifier = modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("PLANT")
                }
            }
        }
    }
}