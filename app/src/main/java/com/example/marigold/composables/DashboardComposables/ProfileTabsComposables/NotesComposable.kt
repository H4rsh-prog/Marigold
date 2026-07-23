package com.example.marigold.composables.DashboardComposables.ProfileTabsComposables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.marigold.R
import com.example.marigold.composables.DashboardComposables.refreshNotes
import com.example.marigold.model.DB
import com.example.marigold.model.Note.Note
import com.example.marigold.ui.component.shapes.cornerPinchedRoundedShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun NotesComposable(revertProfile: () -> Unit, backStack: SnapshotStateList<Any>) {
    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(
            context,
            DB::class.java,
            "marigold_db"
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
        delay(500)
        fell = true
        notes = refreshNotes(dao)
        delay(1000)
        loaded = true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        AnimatedVisibility(
            visible = fell,
            enter = slideInVertically(animationSpec = tween(1000)) {-it}
        ) {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                Column(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.8f)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp,10.dp))
                        .padding(5.dp,0.dp)
                ) {
                    Column(
                        Modifier
                            .wrapContentSize()
                            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp,10.dp))
                            .border(border = BorderStroke(brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, Color.Transparent), start = Offset(100L)), width = 2.dp), RoundedCornerShape(10.dp,10.dp))
                            .padding(10.dp,0.dp)
                    ) {
                        Column(
                            Modifier
                                .wrapContentSize()
                                .background(MaterialTheme.colorScheme.background,RoundedCornerShape(10.dp,10.dp))
                                .padding(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally){
                                Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                    Spacer(Modifier.fillMaxWidth().height(40.dp))
                                    Button(onClick = { newNote = true }) {
                                        Text("ADD NOTE")
                                    }
                                    Spacer(Modifier.height(70.dp))
                                    if(!notes.isNullOrEmpty()) {
                                        notes?.forEachIndexed { index , note ->
                                            AnimatedVisibility(
                                                visible = loaded && note.id != deletingNoteId,
                                                enter = scaleIn(animationSpec = tween(1000, index.times(100))),
                                                exit = scaleOut(animationSpec = tween(500, index.times(100)))
                                            ) {
                                                Column(
                                                    Modifier
                                                        .fillMaxWidth(0.9f)
                                                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp))
                                                        .padding(10.dp)
                                                        .clickable(enabled = true, onClick = {if(selectedNote == note) {selectedNote = null} else {selectedNote = note} })
                                                ) {
                                                    Column(
                                                        Modifier
                                                            .fillMaxSize()
                                                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp))
                                                            .border(2.dp,MaterialTheme.colorScheme.primary,RoundedCornerShape(10.dp))
                                                            .padding(10.dp)
                                                    ) {
                                                        Row{
                                                            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                                                            Spacer(Modifier.weight(1f))
                                                            Icon(painter = painterResource(R.drawable.ic_edit), contentDescription = null, modifier = Modifier.clickable(enabled = true, onClick = {
                                                                selectedNote = note
                                                                updateNote = true
                                                            }))
                                                            Icon(painter = painterResource(R.drawable.ic_delete), contentDescription = null, modifier = Modifier.clickable(enabled = true, onClick = {
                                                                scope.launch {
                                                                    deletingNoteId = note.id
                                                                    dao.deleteNoteById(note.id)
                                                                    delay(500)
                                                                    notes = refreshNotes(dao)
                                                                    selectedNote = null
                                                                    deletingNoteId = null
                                                                }
                                                            }))
                                                        }
                                                        AnimatedVisibility(visible = selectedNote==note) {
                                                            Column{
                                                                Column(
                                                                    Modifier
                                                                        .fillMaxSize()
                                                                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp))
                                                                        .padding(10.dp)
                                                                ){
                                                                    Column(
                                                                        Modifier
                                                                            .fillMaxSize()
                                                                            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(10.dp))
                                                                            .padding(10.dp)
                                                                            .heightIn(max = 500.dp)
                                                                            .verticalScroll(rememberScrollState())
                                                                    ){
                                                                        Text(text = note.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.background)
                                                                    }
                                                                }
                                                                Row{
                                                                    Spacer(Modifier.weight(1f))
                                                                    Text(text = java.sql.Date.from(Instant.ofEpochMilli(note.date.milliseconds.toLong(DurationUnit.MILLISECONDS))).toString(), style = MaterialTheme.typography.titleSmall, modifier = Modifier.scale(0.8f))
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                Spacer(Modifier.height(60.dp))
                                            }
                                        }
                                    }
                                }
                                Spacer(Modifier.height(80.dp))
                                AnimatedVisibility(
                                    visible = !loaded
                                ) {
                                    Spacer(Modifier.fillMaxHeight(0.9f))
                                }
                            }
                        }
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.background,RoundedCornerShape(5.dp,5.dp,10.dp,10.dp))
                        .padding(5.dp)
                ) {
                    Column(
                        Modifier
                            .background(MaterialTheme.colorScheme.secondary,RoundedCornerShape(5.dp,5.dp,10.dp,10.dp))
                            .border(2.dp,MaterialTheme.colorScheme.primary,RoundedCornerShape(5.dp,5.dp,10.dp,10.dp))
                            .padding(5.dp)
                    ){}
                }
                Column(
                    Modifier
                        .fillMaxWidth(0.4f)
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.background,RoundedCornerShape(0.dp,0.dp,10.dp,10.dp))
                        .padding(5.dp)
                ) {
                    Column(
                        Modifier
                            .background(MaterialTheme.colorScheme.secondary,RoundedCornerShape(0.dp,0.dp,10.dp,10.dp))
                            .border(2.dp,MaterialTheme.colorScheme.primary,RoundedCornerShape(0.dp,0.dp,10.dp,10.dp))
                            .padding(5.dp)
                    ) {}
                }
                Spacer(Modifier.height(50.dp))
            }
        }
    }
    AnimatedVisibility(
        visible = newNote || updateNote,
        enter = scaleIn(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(1000))
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Column(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(0.8f)
                    .background(MaterialTheme.colorScheme.background,RoundedCornerShape(0.dp,0.dp,10.dp,10.dp))
                    .padding(5.dp)
            ) {
                Column(
                    Modifier
                        .background(MaterialTheme.colorScheme.secondary,RoundedCornerShape(0.dp,0.dp,10.dp,10.dp))
                        .padding(5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center)
                            .background(MaterialTheme.colorScheme.background, cornerPinchedRoundedShape)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        var title by remember {mutableStateOf(if(updateNote) {selectedNote!!.title} else {""})}
                        var content by remember { mutableStateOf(if(updateNote) {selectedNote!!.content} else {""}) }
                        Row { Spacer(Modifier.weight(1f)); Icon(painter = painterResource(R.drawable.ic_delete), contentDescription = null, modifier = Modifier.clickable(enabled = true, onClick = {newNote = false})) }
                        TextField(
                            value = title,
                            onValueChange = {title = it},
                            label = { Text("Title") },
                            modifier = Modifier.width(400.dp).padding(5.dp).clip(cornerPinchedRoundedShape)
                        )
                        Spacer(Modifier.height(5.dp))
                        TextField(
                            value = content,
                            textStyle = MaterialTheme.typography.bodySmall,
                            onValueChange = {content = it},
                            label = { Text("Content") },
                            modifier = Modifier.width(300.dp).clip(cornerPinchedRoundedShape).heightIn(max = 300.dp).padding(5.dp)
                        )
                        Spacer(Modifier.height(15.dp))
                        Button(
                            onClick = {
                                if(title.isNotBlank() || content.isNotBlank()) {
                                    scope.launch {
                                        if(updateNote) {
                                            dao.upsertNote(Note(id = selectedNote!!.id, title = title, content = content))
                                        } else {
                                            dao.upsertNote(Note(title = title, content = content))
                                        }
                                        notes = refreshNotes(dao)
                                        title = ""
                                        content = ""
                                        newNote = false
                                        updateNote = false
                                        selectedNote = null;
                                    }
                                }
                            },
                            Modifier.scale(0.7f)
                        ) {
                            if(updateNote) {
                                Text("UPDATE")
                            } else {
                                Text("ADD")
                            }
                        }
                    }
                }
            }
        }
    }
}