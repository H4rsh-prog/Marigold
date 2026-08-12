package com.example.marigold.composables

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.room.Room
import com.example.marigold.composables.DashboardComposables.NavScreen
import com.example.marigold.composables.DashboardComposables.ProfileTabs
import com.example.marigold.composables.PreAuthComposables.DefineMarigold
import com.example.marigold.composables.PreAuthComposables.SplashScreen
import com.example.marigold.model.DB
import com.example.marigold.model.Media.Media
import com.example.marigold.model.Media.MediaRemoteDao
import com.example.marigold.model.Memory.Memory
import com.example.marigold.model.Memory.MemoryRemoteDao
import com.example.marigold.model.Note.Note
import com.example.marigold.model.Note.NoteRemoteDao
import com.example.marigold.services.DataHandler
import com.example.marigold.services.RemoteSQLHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class NavigationIndx (val index : Int){
    SPLASH_SCREEN(-1),
    AUTH_SCREEN(0),
    NAV_SCREEN(1)
}
@Composable
fun AppNavigation(
    activity : Activity,
    overrideNavIndx: Int = NavigationIndx.SPLASH_SCREEN.index,
    overrideProfileTabs: ProfileTabs? = null,
    onAuthenticate: (() -> Unit) -> Unit = { it() },
    dataHandler: DataHandler = DataHandler(activity)
) {
    var navIndx by remember { mutableStateOf(overrideNavIndx) }
    var prevNavIndx by remember {mutableStateOf(overrideNavIndx)}
    val overrideNavigationIndx : (NavigationIndx) -> Unit = { destination -> navIndx = destination.index }
    val scope = rememberCoroutineScope()
    remember {
        scope.launch {
            withContext(Dispatchers.IO) {
                refreshDatabases(activity)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        AnimatedContent(
            targetState = navIndx,
            modifier = Modifier.fillMaxSize(),
            label = "navigation",
            transitionSpec = {
                val animationDuration = 800
                ContentTransform(
                    targetContentEnter =
                        if(targetState>prevNavIndx) {
                            slideInVertically(animationSpec = tween(animationDuration)) { it * 2 }
                        } else {
                            scaleIn(animationSpec = tween(animationDuration))
                        },
                    initialContentExit =
                        if(targetState>prevNavIndx) {
                            if(prevNavIndx==NavigationIndx.AUTH_SCREEN.index) {
                                slideOutVertically(tween(animationDuration)) { it }
                            } else {
                                scaleOut(tween(animationDuration), targetScale = 2.5F,)
                            }
                        } else {
                            slideOutVertically(tween(animationDuration)) { it*2 }
                        }
                )
            }
        ) {
            prevNavIndx = it
            when (it) {
                -1 ->
                    SplashScreen(
                        resolveView = { onAuthenticate { navIndx = NavigationIndx.AUTH_SCREEN.index; } }
                    )
                0 ->
                    DefineMarigold(
                        resolveView = { navIndx = NavigationIndx.NAV_SCREEN.index; },
                        isInitialized = dataHandler.isAppInitialized()
                    )
                1 ->
                    Box(modifier = Modifier.fillMaxSize().background(brush = Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary)), alpha = 0.6f)){
                        NavScreen(
                            overrideNavigationIndx = overrideNavigationIndx,
                            overrideProfileTabs = overrideProfileTabs
                        )
                    }
            }
        }
    }
}

suspend public fun refreshDatabases(context : Context, entity_type : Any? = null){
    val db = Room.databaseBuilder(
        context = context,
        klass = DB::class.java,
        name = DB.DB_NAME
    ).build()
    if(Looper.myLooper()==null) Looper.prepare()
    if(RemoteSQLHandler().getSQLConnection()==null) {
        Toast.makeText(context, "Databases couldn't be refreshed due to some connectivity issues", Toast.LENGTH_SHORT).show()
        return
    }
    println("INITIATED REFRESHMENT")
    if(entity_type != null) {
        when(entity_type) {
            Memory::class -> {
                println("REFRESHING MEMORIES")
                db.memoryDAO().deleteAll()
                db.memoryDAO().upsertAll(MemoryRemoteDao().fetch())
                println("REFRESHED MEMORIES")
                Toast.makeText(context, "Memories Refreshed", Toast.LENGTH_SHORT).show()
            }
            Note::class -> {
                println("REFRESHING JOURNAL")
                db.noteDAO().deleteAll()
                db.noteDAO().upsertAll(NoteRemoteDao().fetch())
                println("REFRESHED JOURNAL")
                Toast.makeText(context, "Notes Refreshed", Toast.LENGTH_SHORT).show()
            }
            Media::class -> {
                println("REFRESHING MEDIA")
                db.mediaDAO().deleteAll()
                db.mediaDAO().upsertAll(MediaRemoteDao().fetch())
                println("REFRESHED MEDIA")
                Toast.makeText(context, "Media Refreshed", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        println("REFRESHING ALL DATABASES")
        db.noteDAO().deleteAll()
        db.memoryDAO().deleteAll()
        db.memoryDAO().upsertAll(MemoryRemoteDao().fetch())
        db.noteDAO().upsertAll(NoteRemoteDao().fetch())
        db.mediaDAO().upsertAll(MediaRemoteDao().fetch())
        println("REFRESHED ALL DATABASES")
    }
}