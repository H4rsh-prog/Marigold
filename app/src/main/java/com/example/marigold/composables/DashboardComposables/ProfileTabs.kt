package com.example.marigold.composables.DashboardComposables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.marigold.composables.DashboardComposables.ProfileTabsComposables.MediaComposable
import com.example.marigold.composables.DashboardComposables.ProfileTabsComposables.NotesComposable
import com.example.marigold.model.Media.Media
import com.example.marigold.model.Media.MediaDAO
import com.example.marigold.model.Note.Note
import com.example.marigold.model.Note.NoteDAO

enum class ProfileTabs(
    val label: String,
    val content: @Composable (revertProfile: () -> Unit, backStack: SnapshotStateList<Any>) -> Unit,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    NOTES("Notes", {revertProfile, backStack -> NotesComposable(revertProfile, backStack)}),
    MEDIA("Media", {revertProfile, backStack -> MediaComposable(revertProfile, backStack)}),
    MEMORIES("Memories", { revertProfile, backStack ->
        Button(onClick = revertProfile) {
            Text("RETURN");
        }
    });
}

suspend fun refreshNotes(dao: NoteDAO): List<Note>{
    return dao.getAllNotes().sortedByDescending { note -> note.date }
}
suspend fun refreshMedia(dao: MediaDAO): List<Media>{
    return dao.getAllMedia().sortedByDescending { media -> media.date }
}