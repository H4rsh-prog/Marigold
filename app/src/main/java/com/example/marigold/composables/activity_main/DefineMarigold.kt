package com.example.marigold.composables.activity_main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun DefineMarigold(authorizedView : () -> Unit = {}, modifier : Modifier = Modifier){
    var visible_main by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        delay(1000)
        visible_main = true;
    }
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(2000))
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
                .verticalScroll(state = rememberScrollState(), enabled = true),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
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
                    onValueChange = {
                        text -> run {
                            input = text;
                            if(input.equals("userpass")) {
                                authorizedView();
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(focusedTextColor = MaterialTheme.colorScheme.secondary),
                    label = { Text(text="Define Marigold", style = MaterialTheme.typography.titleSmall) },
                    modifier = modifier.fillMaxSize()
                )
            }
        }
    }
}