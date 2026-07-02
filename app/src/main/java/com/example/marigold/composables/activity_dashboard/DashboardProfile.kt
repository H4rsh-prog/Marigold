package com.example.marigold.composables.activity_dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DashboardProfile(modifier: Modifier = Modifier) {
    var profile by remember { mutableStateOf(null as ProfileTabs?) }
    AnimatedVisibility(
        visible = profile == null,
        enter = fadeIn(animationSpec = tween(1800)),
        exit = fadeOut(animationSpec = tween(1000)),
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            contentPadding = PaddingValues(40.dp)
        ) {
            items(ProfileTabs.entries) { item ->
                Card(
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 4.dp, bottomStart = 4.dp, bottomEnd = 24.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth(),
                    onClick =  {
                        profile = item;
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
    AnimatedVisibility(
        visible = profile != null,
        enter = fadeIn(animationSpec = tween(1000)),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            profile?.content({ profile = null });
        }
    }
}

enum class ProfileTabs(
    val label: String,
    val content: @Composable (revertProfile: () -> Unit) -> Unit
) {
    PHRASES("Phrases", {
        Button(onClick = it) {
            Text("RETURN");
        }
    }),
    CALENDAR("Calender", {
        Button(onClick = it) {
            Text("RETURN");
        }
    }),
    MEDIA("Media", {
        Button(onClick = it) {
            Text("RETURN");
        }
    }),
    MEMORIES("Memories", {
        Button(onClick = it) {
            Text("RETURN");
        }
    });
}