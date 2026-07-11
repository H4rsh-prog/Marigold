package com.example.marigold.composables.activity_dashboard

import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import com.example.marigold.R
import com.example.marigold.model.DB
import com.example.marigold.model.Media.Media
import com.example.marigold.model.Note.Note
import com.example.marigold.model.Note.NoteDAO
import com.example.marigold.ui.component.shapes.cornerPinchedRoundedShape
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

enum class ProfileTabs(
    val label: String,
    val content: @Composable (revertProfile: () -> Unit) -> Unit
) {
    @RequiresApi(Build.VERSION_CODES.O)
    NOTES("Notes", {
        val context = LocalContext.current
        val db = remember {
            Room.databaseBuilder(
                context,
                DB::class.java,
                "tbl_notes"
            ).build()
        }
        val dao = remember { db.noteDAO() }
        val scope = rememberCoroutineScope()
        var notes by remember { mutableStateOf(null as List<Note>?) }
        var selectedNoteId by remember { mutableStateOf(null as String?) }
        LaunchedEffect(Unit) {
            notes = refreshNotes(dao)
        }
        Card (modifier = Modifier
            .border(2.dp, MaterialTheme.colorScheme.primary)
            .scale(0.85f)
            .border(2.dp, MaterialTheme.colorScheme.primary, cornerPinchedRoundedShape), shape = cornerPinchedRoundedShape) {
            Row(horizontalArrangement = Arrangement.End){
                Spacer(Modifier.weight(0.7f))
                Button(
                    onClick = it,
                    modifier = Modifier
                        .weight(0.3f)
                        .scale(0.8f)
                ) {
                    Text("RETURN")
                }
            }
            Box(modifier = Modifier.padding(18.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    var title by remember { mutableStateOf("") }
                    var contentText by remember { mutableStateOf("") }
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        value = contentText,
                        onValueChange = { contentText = it },
                        label = { Text("Content") },
                        modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.primary).heightIn(max = 300.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() || contentText.isNotBlank()) {
                                scope.launch {
                                    dao.upsertNote(Note(title = title, content = contentText))
                                    notes = refreshNotes(dao)
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
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(30.dp)
        ) {
            if(!notes.isNullOrEmpty()) {
                item{
                    Text(
                        text = "NOTES",
                        style = MaterialTheme.typography.headlineMedium,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.padding(vertical = 8.dp)
                    );
                    Spacer(modifier = Modifier.padding(10.dp))
                }
                items(notes!!) { note ->
                    Column{
                        Card(
                            shape = cornerPinchedRoundedShape,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth()
                                .border(
                                    2.dp,
                                    MaterialTheme.colorScheme.primary,
                                    cornerPinchedRoundedShape
                                ),
                        ) {
                            Column(
                                modifier =
                                    if (selectedNoteId == note.id) {
                                        Modifier
                                            .background(MaterialTheme.colorScheme.primary)
                                            .padding(12.dp)
                                    } else {
                                        Modifier.padding(18.dp)
                                    }
                                        .clickable(enabled = true, onClick = {
                                            selectedNoteId = if (selectedNoteId == note.id) {
                                                null
                                            } else {
                                                note.id
                                            }
                                        })
                            ) {
                                Text(
                                    text = note.title,
                                    style = if(selectedNoteId==note.id) {MaterialTheme.typography.titleSmall} else {MaterialTheme.typography.titleMedium},
                                    textAlign = TextAlign.Start,
                                    color = if (selectedNoteId == note.id) {
                                        MaterialTheme.colorScheme.background
                                    } else {
                                        Color.Unspecified
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
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
                                            .border(
                                                2.dp,
                                                MaterialTheme.colorScheme.primary,
                                                RoundedCornerShape(24.dp)
                                            )
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = note.content,
                                                style = MaterialTheme.typography.bodyLarge,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp)
                                                    .weight(0.8f)
                                            )
                                            Button(
                                                onClick = {
                                                    scope.launch {
                                                        dao.deleteNoteById(note.id)
                                                        notes = dao.getAllNotes()
                                                        selectedNoteId = null
                                                    }
                                                },
                                                modifier = Modifier
                                                    .weight(0.3f)
                                                    .scale(0.5f)
                                            ) {
                                                Text("DELETE")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Row {
                            Spacer(modifier = Modifier.weight(0.7f))
                            Text(
                                text = java.sql.Date.from(Instant.ofEpochMilli(note.date.milliseconds.toLong(DurationUnit.MILLISECONDS))).toString(),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }),
    MEDIA("Media", { revertProfile ->
        val context = LocalContext.current
        val db = remember {
            Room.databaseBuilder(
                context = context,
                klass = DB::class.java,
                name = "tbl_media"
            ).build()
        }
        val dao = remember { db.mediaDAO() }
        val scope = rememberCoroutineScope()
        var mediaItems by remember { mutableStateOf(null as List<Media>?) }
        LaunchedEffect(Unit) {
            mediaItems = dao.getAllMedia()
        }
        val mediaPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia()
        ) { uris ->
            if (uris.isNotEmpty()) {
                scope.launch {
                    uris.forEach { uri ->
                        context.contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        dao.upsertMedia(Media(uri = uri.toString()))
                    }
                    mediaItems = dao.getAllMedia()
                }
            }
        }
        var previewUri by remember { mutableStateOf(null as String?) }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = revertProfile) {
                Text("RETURN")
            }
            AnimatedVisibility(
                visible = !previewUri.isNullOrEmpty(),
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut(animationSpec = tween(1000)),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(previewUri?.toUri()),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                        .clickable(enabled = true, onClick = { previewUri = null }),
                )
            }
            AnimatedVisibility(
                visible = previewUri.isNullOrEmpty(),
                enter = fadeIn(animationSpec = tween(1000)),
                exit = fadeOut(animationSpec = tween(1000)),
            ) {
                LazyVerticalGrid(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    columns = GridCells.Adaptive(minSize = 120.dp),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    if (!mediaItems.isNullOrEmpty()) {
                        items(mediaItems!!) { media ->
                            AnimatedVisibility(
                                visible = previewUri.isNullOrEmpty(),
                                enter = fadeIn(animationSpec = tween(1000)),
                                exit = fadeOut(animationSpec = tween(1000)),
                            ) {
                                Card(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .height(80.dp)
                                        .clickable(enabled = true, onClick = { previewUri = media.uri }),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(media.uri.toUri()),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.BottomEnd) {
                    Button(onClick = {
                        mediaPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) {
                        Icon(painter = painterResource(R.drawable.outline_add_24), contentDescription = "Add Photo")
                    }
                }
            }
        }
    }),
    MEMORIES("Memories", {
        Button(onClick = it) {
            Text("RETURN");
        }
    });
}

suspend fun refreshNotes(dao: NoteDAO): List<Note>?{
    return dao.getAllNotes().sortedByDescending { note-> note.date }
}