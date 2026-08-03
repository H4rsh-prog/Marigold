package com.example.marigold.composables.DashboardComposables.ProfileTabsComposables

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marigold.model.Memory.Memory
import com.example.marigold.model.Memory.MemoryRemoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MemoriesComposable(revertProfile: () -> Unit, backStack: SnapshotStateList<Any>) {
    val remote = remember { MemoryRemoteDao() }
    var memories by remember { mutableStateOf(emptyList<Memory>()) }
    val scope = rememberCoroutineScope()
    var updateSelect by remember { mutableStateOf(null as Memory?) }
    var showcasedMemory by remember { mutableStateOf(null as Memory?) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            memories = remote.fetch()
            if (memories.isNotEmpty()) showcasedMemory = memories.random()
        }
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        if (updateSelect != null) {
            var id by remember { mutableStateOf(updateSelect!!.id) }
            var content by remember { mutableStateOf(updateSelect!!.memory) }
            var date by remember { mutableStateOf(updateSelect!!.date) }
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Edit Memory", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text(
                    "DATE : " + SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(
                        Date(
                            date
                        )
                    ), style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = content,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    onValueChange = { content = it },
                    label = { Text("What happened?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .heightIn(min = 150.dp, max = 300.dp)
                )
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    val updated = Memory(id = id, memory = content, date = date)
                                    remote.update(updated)
                                    memories = remote.fetch()
                                    showcasedMemory = updated
                                    updateSelect = null
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("SAVE CHANGES") }
                    Button(
                        onClick = { updateSelect = null },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) { Text("CANCEL") }
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (showcasedMemory != null) {
                    CoreMemoryShowcase(
                        memory = showcasedMemory!!,
                        onRefresh = {
                            if (memories.isNotEmpty()) showcasedMemory = memories.random()
                        },
                        onEdit = { updateSelect = showcasedMemory },
                        onDelete = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    remote.remove(showcasedMemory!!.id)
                                    memories = remote.fetch()
                                    showcasedMemory =
                                        if (memories.isNotEmpty()) memories.random() else null
                                }
                            }
                        }
                    )
                } else {
                    Text(
                        "No memories found. Start creating your story.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
                Spacer(Modifier.height(24.dp))
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
                            withContext(Dispatchers.IO) {
                                val newMemory = Memory(
                                    id = java.util.UUID.randomUUID().toString(),
                                    memory = "Captured a beautiful moment...", // Placeholder or prompt
                                    date = System.currentTimeMillis()
                                )
                                remote.add(newMemory)
                                memories = remote.fetch()
                                showcasedMemory = newMemory
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
                            Text("CAPTURE A CORE MEMORY", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CoreMemoryShowcase(
    memory: Memory,
    onRefresh: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
            Box() {
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
                        text = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(memory.date)),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier
                            .size(44.dp)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = "Refresh",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
                Spacer(Modifier.height(32.dp))
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "“",
                        fontFamily = FontFamily.Cursive,
                        fontSize = 100.sp,
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = (-16).dp, y = (-40).dp)
                    )

                    Text(
                        text = memory.memory,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Text(
                        text = "”",
                        fontFamily = FontFamily.Cursive,
                        fontSize = 100.sp,
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 0.dp, y = 100.dp)
                    )
                }
                Spacer(Modifier.height(40.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}
