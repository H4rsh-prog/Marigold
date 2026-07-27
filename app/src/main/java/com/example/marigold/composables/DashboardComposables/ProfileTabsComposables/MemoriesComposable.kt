package com.example.marigold.composables.DashboardComposables.ProfileTabsComposables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.example.marigold.model.Memory.Memory
import com.example.marigold.model.Memory.MemoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@Composable
fun MemoriesComposable(revertProfile: () -> Unit, backStack: SnapshotStateList<Any>) {
    val dao = remember { MemoryDao() }
    var memories by remember { mutableStateOf(emptyList<Memory>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            memories = dao.fetchMemories()
        }
    }

    Column() {
        memories.forEach {
            Text("ID: ${it.id}, Memory: ${it.memory}")
        }
    }
    Button(
        enabled = true,
        onClick = {
            scope.launch {
                withContext(Dispatchers.IO) {
                    dao.addMemory(
                        Memory(
                            id = UUID.randomUUID().toString(),
                            memory = LoremIpsum(50).values.joinToString(" ")
                        )
                    )
                    memories = dao.fetchMemories()
                }
            }
        }
    ) {
        Text("ADD ENTRY")
    }
}