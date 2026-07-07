package com.example.marigold.composables.activity_dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.marigold.model.Note.Note
import com.example.marigold.model.Note.NoteDB
import kotlinx.coroutines.launch

@Composable
fun DashboardProfile(modifier: Modifier = Modifier, profileOverride: ProfileTabs? = null) {
    var profile by remember { mutableStateOf(profileOverride as ProfileTabs?) }
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
    NOTES("Notes", {
        val context = LocalContext.current
        val db = remember {
            Room.databaseBuilder(
                context,
                NoteDB::class.java,
                "tbl_notes"
            ).build()
        }
        val dao = remember { db.roomDAO() }
        val scope = rememberCoroutineScope()
        var notes by remember { mutableStateOf(null as List<Note>?) }
        var selectedNoteId by remember { mutableStateOf(null as String?) }
        LaunchedEffect(Unit) {
            notes = dao.getAllNotes();
        }
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(30.dp)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.End){
                    Spacer(Modifier.weight(0.7f))
                    Button(
                        onClick = it,
                        modifier = Modifier.weight(0.3f).scale(0.8f)
                    ) {
                        Text("RETURN")
                    }
                }
            }
            item {
                Card(
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 4.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 24.dp
                    ),
                    modifier = Modifier
                        .padding(0.dp, 10.dp)
                        .fillMaxWidth()
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 4.dp,
                            bottomStart = 4.dp,
                            bottomEnd = 24.dp
                        ))
                ) {
                    Box(modifier = Modifier.padding(18.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            var title by remember { mutableStateOf("") }
                            var contentText by remember { mutableStateOf("") }
                            TextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.primary))
                            Spacer(modifier = Modifier.height(10.dp))
                            TextField(value = contentText, onValueChange = { contentText = it }, label = { Text("Content") }, modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.primary))
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    if (title.isNotBlank() || contentText.isNotBlank()) {
                                        scope.launch {
                                            dao.upsertNote(Note(title = title, content = contentText))
                                            notes = dao.getAllNotes()
                                            title = ""
                                            contentText = ""
                                        }
                                    }
                                },
                                modifier = Modifier.scale(0.6f)
                            ) {
                                Text(text = "Add Note",
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
            if(!notes.isNullOrEmpty()) {
                item{
                    Text("NOTES", style = MaterialTheme.typography.headlineSmall, textDecoration = TextDecoration.Underline, modifier = Modifier.padding(vertical = 8.dp));
                    Spacer(modifier = Modifier.padding(10.dp))
                }
                items(notes!!) { note ->
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 4.dp,
                            bottomStart = 4.dp,
                            bottomEnd = 24.dp
                        ),
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                            .fillMaxWidth()
                            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(
                                topStart = 24.dp,
                                topEnd = 4.dp,
                                bottomStart = 4.dp,
                                bottomEnd = 24.dp
                            )),
                        onClick = {
                            selectedNoteId = note.id
                        }
                    ) {
                        Column(
                            modifier =
                                if(selectedNoteId==note.id) { Modifier
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(18.dp)
                                } else { Modifier.padding(18.dp) }
                        ) {
                            Text(
                                text = note.title,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                color = if(selectedNoteId==note.id) { MaterialTheme.colorScheme.background} else {Color.Unspecified},
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedVisibility(
                                visible = selectedNoteId == note.id,
                                enter = fadeIn(animationSpec = tween(1000)),
                                exit = fadeOut(animationSpec = tween(1000)),
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Bottom,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(2.dp)
                                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(24.dp))
                                ){
                                    Text(
                                        text = note.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                    )
                                    Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom){
                                        Spacer(Modifier.weight(0.7f))
                                        Button(onClick = {
                                            scope.launch {
                                                dao.deleteNoteById(note.id)
                                                notes = dao.getAllNotes()
                                                selectedNoteId = null
                                            }
                                        },
                                            modifier = Modifier.weight(0.3f).scale(0.5f)
                                        ) {
                                            Text("DELETE")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
