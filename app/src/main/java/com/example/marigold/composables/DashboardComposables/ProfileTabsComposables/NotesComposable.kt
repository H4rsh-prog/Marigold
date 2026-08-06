package com.example.marigold.composables.DashboardComposables.ProfileTabsComposables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.marigold.model.DB
import com.example.marigold.model.Note.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun NotesComposable(revertProfile: () -> Unit, backStack: SnapshotStateList<Any>) {
    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(
            context = context,
            klass = DB::class.java,
            name = DB.DB_NAME
        ).createFromAsset("databases/initMarigold.db").build()
    }
    val dao = remember { db.noteDAO() }
    val scope = rememberCoroutineScope()
    var notes by remember { mutableStateOf(null as List<Note>?) }
    var newNote by remember { mutableStateOf(false) }
    var updateNote by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf(null as Note?) }
    var loaded by remember { mutableStateOf(false) }
    var fell by remember { mutableStateOf(false) }
    var deletingNoteId by remember { mutableStateOf(null as String?) }
    LaunchedEffect(Unit) {
        delay(300)
        fell = true
        notes = dao.getAll().sortedByDescending { it.date }
        delay(800)
        loaded = true
    }
    val paperColor = Color(0xFFFFF9E6)
    val scrollEdgeBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f),
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f),
            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = fell,
                enter = slideInVertically(animationSpec = tween(1200)) { -it } + fadeIn(tween(1200))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(30.dp)
                            .shadow(8.dp, RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                            .background(scrollEdgeBrush, RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .background(paperColor)
                            .border(
                                BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                                )
                            )
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "My Journal",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        Button(
                            onClick = { newNote = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(bottom = 32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("NEW ENTRY")
                        }
                        if (!notes.isNullOrEmpty()) {
                            notes?.forEachIndexed { index, note ->
                                AnimatedVisibility(
                                    visible = loaded && note.id != deletingNoteId,
                                    enter = scaleIn(animationSpec = tween(600, index * 80)) + fadeIn(),
                                    exit = scaleOut(animationSpec = tween(400)) + fadeOut()
                                ) {
                                    NoteItem(
                                        note = note,
                                        isSelected = selectedNote == note,
                                        onToggle = {
                                            selectedNote = if (selectedNote == note) null else note
                                        },
                                        onEdit = {
                                            selectedNote = note
                                            updateNote = true
                                        },
                                        onDelete = {
                                            scope.launch {
                                                deletingNoteId = note.id
                                                dao.deleteById(note.id)
                                                delay(400)
                                                notes = dao.getAll().sortedByDescending { it.date }
                                                selectedNote = null
                                                deletingNoteId = null
                                            }
                                        }
                                    )
                                }
                                Spacer(Modifier.height(16.dp))
                            }
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .height(20.dp)
                                .background(paperColor.copy(alpha = 0.9f), RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                                .shadow(4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(15.dp)
                                .background(paperColor.copy(alpha = 0.7f), RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                                .shadow(2.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(25.dp)
                                .shadow(8.dp, RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                                .background(scrollEdgeBrush, RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        )
                    }
                    Spacer(Modifier.height(80.dp))
                }
            }
        }
        AnimatedVisibility(
            visible = newNote || updateNote,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 0.8f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { 
                        newNote = false
                        updateNote = false
                        selectedNote = null
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(paperColor)
                        .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                        .padding(24.dp)
                        .clickable(enabled = false) {},
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var title by remember(newNote, updateNote) { 
                        mutableStateOf(if (updateNote) selectedNote?.title ?: "" else "") 
                    }
                    var content by remember(newNote, updateNote) { 
                        mutableStateOf(if (updateNote) selectedNote?.content ?: "" else "") 
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (updateNote) "Edit Note" else "New Note",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(onClick = { 
                            newNote = false
                            updateNote = false
                            selectedNote = null
                        }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Topic") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                    TextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Write your thoughts...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp, max = 300.dp),
                        textStyle = TextStyle(fontFamily = FontFamily.Cursive, fontSize = 20.sp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() || content.isNotBlank()) {
                                scope.launch {
                                    if (updateNote) {
                                        dao.upsert(Note(id = selectedNote!!.id, title = title, content = content))
                                    } else {
                                        dao.upsert(Note(title = title, content = content))
                                    }
                                    notes = dao.getAll().sortedByDescending { it.date }
                                    newNote = false
                                    updateNote = false
                                    selectedNote = null
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(if (updateNote) "UPDATE" else "SAVE")
                    }
                }
            }
        }
    }
}
@Composable
fun NoteItem(
    note: Note,
    isSelected: Boolean,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isSelected) 8.dp else 2.dp, RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .border(
                if (isSelected) 2.dp else 1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onToggle)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
            }
        }
        AnimatedVisibility(
            visible = isSelected,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 0.5.dp,
                    color = Color.LightGray
                )
                Text(
                    text = note.content,
                    style = TextStyle(
                        fontFamily = FontFamily.Cursive,
                        fontSize = 20.sp,
                        lineHeight = 28.sp
                    ),
                    color = Color.DarkGray,
                    modifier = Modifier.height(400.dp).verticalScroll(rememberScrollState())
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(Date(note.date)),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
