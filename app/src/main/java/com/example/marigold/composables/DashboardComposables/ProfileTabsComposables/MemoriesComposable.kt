package com.example.marigold.composables.DashboardComposables.ProfileTabsComposables

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.marigold.model.DB
import com.example.marigold.model.Memory.Memory
import com.example.marigold.model.Memory.MemoryRemoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MemoriesComposable(revertProfile: () -> Unit, backStack: SnapshotStateList<Any> ) {
    val remote = remember { MemoryRemoteDao() }
    val context = LocalContext.current
    val dao = remember {
        Room.databaseBuilder(
            context = context,
            klass = DB::class.java,
            name = DB.DB_NAME
        ).build().memoryDAO()
    }
    val scope = rememberCoroutineScope()
    var memories by remember { mutableStateOf(emptyList<Memory>()) }
    var showcasedMemory by remember { mutableStateOf(null as Memory?) }
    var loaded by remember { mutableStateOf(false) }
    var memorySwitch by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        memories = dao.getAll()
        showcasedMemory = memories.random()
        delay(300)
        loaded = true
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = loaded,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    onClick = {
                        scope.launch {
                            if (memories.isNotEmpty()) {
                                val remainingMemories = memories.filter { memory -> memory != showcasedMemory }
                                showcasedMemory = if(remainingMemories.isNotEmpty()) { remainingMemories.random() } else { showcasedMemory }
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.tertiary
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AddCircleOutline, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(12.dp))
                            Text("TRY TO RECALL SOMETHING ELSE", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp, color = Color.White)
                        }
                    }
                }
            }
            AnimatedContent(
                targetState = showcasedMemory,
                label = "CoreMemoryShowcase",
                transitionSpec = {
                    ContentTransform(
                        targetContentEnter = scaleIn(animationSpec = tween(1000)) + fadeIn(animationSpec = tween(500)),
                        initialContentExit = slideOutVertically{-it} + fadeOut(animationSpec = tween(500))
                    )
                }
            ) {
                if (it != null) {
                    loaded = true
                    CoreMemoryShowcase(
                        memory = it,
                        onReload = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    memories = remote.fetch()
                                    Toast.makeText(context, "Memories Refreshed", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    )
                } else {
                    loaded = false
                    Text(
                        "Cant Remember Anything...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun CoreMemoryShowcase(
    memory: Memory,
    onReload: () -> Unit
) {
    val marigoldThemeGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f), // GoldenAccent
            Color(0xFFF5EBE0),                                      // Warm Sand
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)   // MarigoldOrange
        )
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(32.dp),
                ambientColor = MaterialTheme.colorScheme.tertiary,
                spotColor = MaterialTheme.colorScheme.primary
            ),
        shape = RoundedCornerShape(32.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(marigoldThemeGradient)
                .padding(32.dp)
        ) {
            Box {
                Icon(
                    painter = painterResource(com.example.marigold.R.drawable.lamare_della_mi_vita_trasparent), contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f).copy(alpha = 0.25f),
                    modifier = Modifier.height(500.dp).align(Alignment.Center).offset(x = 0.dp, y=100.dp).scale(2f)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = SimpleDateFormat("MMMM dd, yyyy, HH:mm", Locale.getDefault()).format(Date(memory.date)),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    IconButton(
                        onClick = onReload,
                        modifier = Modifier
                            .size(44.dp)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = "Reload",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
                Spacer(Modifier.height(60.dp))
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "“",
                        fontFamily = FontFamily.Cursive,
                        fontSize = 100.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = (-16).dp, y = (-40).dp)
                    )
                    Text(
                        text = memory.memory,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp).height(400.dp)
                            .verticalScroll(rememberScrollState())
                    )
                    Text(
                        text = "”",
                        fontFamily = FontFamily.Cursive,
                        fontSize = 100.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 0.dp, y = 100.dp)
                    )
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}
