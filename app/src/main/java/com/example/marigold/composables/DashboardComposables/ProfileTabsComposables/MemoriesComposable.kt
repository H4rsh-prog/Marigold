package com.example.marigold.composables.DashboardComposables.ProfileTabsComposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.example.marigold.R
import com.example.marigold.model.Memory.Memory
import com.example.marigold.model.Memory.MemoryRemoteDao
import com.example.marigold.ui.component.shapes.cornerPinchedRoundedShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

@Composable
fun MemoriesComposable(revertProfile: () -> Unit, backStack: SnapshotStateList<Any>) {
    val remote = remember { MemoryRemoteDao() }
    var memories by remember { mutableStateOf(emptyList<Memory>()) }
    val scope = rememberCoroutineScope()
    var updateSelect by remember { mutableStateOf(null as Memory?) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            memories = remote.fetch()
        }
    }

    if(updateSelect!=null) {
        var id by remember {mutableStateOf(updateSelect!!.id)}
        var content by remember { mutableStateOf(updateSelect!!.memory) }
        var date by remember { mutableStateOf(updateSelect!!.date) }
        Spacer(Modifier.height(5.dp))
        Text("ID : "+id)
        Text("DATE : "+date)
        TextField(
            value = content,
            textStyle = MaterialTheme.typography.bodySmall,
            onValueChange = {content = it},
            label = { Text("Content") },
            modifier = Modifier.width(300.dp).clip(cornerPinchedRoundedShape).heightIn(max = 300.dp).padding(5.dp)
        )
        Spacer(Modifier.height(15.dp))
        Button(onClick = {
            scope.launch {
                withContext(Dispatchers.IO) {
                    remote.update(
                        Memory(
                            id = id,
                            memory = content,
                            date = date
                        )
                    )
                    memories = remote.fetch()
                    updateSelect = null
                }
            }
        }) {  Text("UPDATE") }
    } else {
    Column() {
        memories.forEach {
            Text("ID: ${it.id}, Memory: ${it.memory}")
            Icon(painter = painterResource(R.drawable.ic_edit), contentDescription = null, modifier = Modifier.clickable(enabled = true, onClick = {
                updateSelect = it;
            }))
            Icon(painter = painterResource(R.drawable.ic_delete), contentDescription = null, modifier = Modifier.clickable(enabled = true, onClick = {
                scope.launch {
                    withContext(Dispatchers.IO) {
                        remote.remove(it.id)
                        memories = remote.fetch()
                    }
                }
            }))
        }
    }
    Button(
        enabled = true,
        onClick = {
            scope.launch {
                withContext(Dispatchers.IO) {
                    remote.add(
                        Memory(
                            id = UUID.randomUUID().toString(),
                            memory = LoremIpsum(50).values.joinToString(" ")
                        )
                    )
                    memories = remote.fetch()
                }
            }
        }
    ) {
        Text("ADD ENTRY")
    }
    }
}