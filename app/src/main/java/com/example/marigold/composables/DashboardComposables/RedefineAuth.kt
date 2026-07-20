package com.example.marigold.composables.DashboardComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.marigold.R

@Composable
fun RedefineAuth(resolveView: () -> Unit = {}, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Column(modifier.wrapContentSize().background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(30.dp)).padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Column(modifier.wrapContentSize().background(MaterialTheme.colorScheme.background, RoundedCornerShape(30.dp)).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Column(
                    modifier = modifier
                        .wrapContentSize()
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(30.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(30.dp))
                        .padding(24.dp)
                        .verticalScroll(state = rememberScrollState(), enabled = true),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Card(
                        modifier = modifier
                            .width(300.dp)
                            .wrapContentHeight()
                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                        )
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = modifier.wrapContentSize()) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = modifier.padding(16.dp, 0.dp, 16.dp, 16.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.lamare_della_mi_vita_trasparent),
                                    contentDescription = "Flower Placeholder",
                                    modifier = modifier.scale(0.95f).size(300.dp)
                                )
                                Text(
                                    text = "May this marigold remind you of the gentle moments of happiness in your life. Because when you smile, you make everything around you as bright as summer vibes.",
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = {resolveView()},
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Redefine Marigold", color = Color.White)
                }
            }
        }
    }
}