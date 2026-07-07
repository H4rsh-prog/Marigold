package com.example.marigold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Density
import androidx.room.Room
import com.example.marigold.composables.activity_dashboard.Dashboard
import com.example.marigold.composables.activity_dashboard.DashboardProfile
import com.example.marigold.composables.activity_dashboard.ProfileTabs
import com.example.marigold.model.Note.Note
import com.example.marigold.model.Note.NoteDB
import com.example.marigold.ui.theme.MarigoldTheme

class DashboardActivity : ComponentActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarigoldTheme {
                val density = LocalDensity.current
                val fontScale = minOf(density.fontScale, 1.3f)
                CompositionLocalProvider(
                    LocalDensity provides Density(
                        density = density.density,
                        fontScale = fontScale
                    )
                ) {Dashboard()}
            }
        }
    }
}

@PreviewLightDark
@Composable
fun GreetingPreview2() {
    loadData()
    DashboardProfile(profileOverride = ProfileTabs.NOTES);
}

@Composable
fun loadData(){
    val context = LocalContext.current;
    val db = remember {
        Room.databaseBuilder(
            context,
            NoteDB::class.java,
            "tbl_notes"
        ).build()
    }
    val scope = rememberCoroutineScope();
    val dao = db.roomDAO();
    LaunchedEffect(Unit) {
        dao.upsertNote(Note(title = "Hello", content = "World"));
        dao.upsertNote(Note(title = "Test", content = "Test"));
    }
}