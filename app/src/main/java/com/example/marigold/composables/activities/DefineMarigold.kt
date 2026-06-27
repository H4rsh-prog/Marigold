package com.example.marigold.composables.activities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

@Composable
public fun DefineMarigold(nextView : () -> Unit = {}, modifier : Modifier = Modifier){
    var visible_main by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        delay(1000)
        visible_main = true;
    }
    AnimatedVisibility(
        visible = visible_main,
        enter = fadeIn(animationSpec = tween(2000))
    ) {
        Box(contentAlignment = Alignment.Center) {
            TextField(
                value = input,
                onValueChange = { t ->
                    run {
                        input = t;
                        if (input.equals("lamare")) {
                            nextView()
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
public fun DefineMarigoldPreview() {
    DefineMarigold()
}
