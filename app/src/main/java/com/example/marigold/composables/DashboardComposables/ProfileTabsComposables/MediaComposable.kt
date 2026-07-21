package com.example.marigold.composables.DashboardComposables.ProfileTabsComposables

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import com.example.marigold.R
import com.example.marigold.composables.DashboardComposables.ProfileTabs
import com.example.marigold.composables.DashboardComposables.refreshMedia
import com.example.marigold.model.DB
import com.example.marigold.model.Media.Media
import kotlinx.coroutines.launch

@Composable
fun MediaComposable(revertProfile : () -> Unit, backStack: SnapshotStateList<Any>){
    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(
            context = context,
            klass = DB::class.java,
            name = "marigold_db"
        ).build()
    }
    val dao = remember { db.mediaDAO() }
    val scope = rememberCoroutineScope()
    var mediaItems by remember { mutableStateOf(null as List<Media>?) }
    LaunchedEffect(Unit) {
        mediaItems = refreshMedia(dao)
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
                mediaItems = refreshMedia(dao);
            }
        }
    }
    var previewMedia by remember { mutableStateOf(null as Media?) }
    AnimatedContent(
        targetState = previewMedia,
        label = "media",
        transitionSpec = {
            ContentTransform(
                targetContentEnter = scaleIn(tween(1000)),
                initialContentExit = scaleOut(tween(1000))
            )
        },
        content = { preview ->
            if (preview != null) {
                Image(
                    painter = rememberAsyncImagePainter(preview.uri.toUri()),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = true, onClick = { previewMedia = null })
                        .background(Color.Black),
                )
            } else {
                LazyVerticalGrid(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    contentPadding = PaddingValues(8.dp),
                ) {
                    if (!mediaItems.isNullOrEmpty()) {
                        (mediaItems as Iterable<Media?>).forEachIndexed { index, media ->
                            item {
                                AnimatedVisibility(
                                    visible = previewMedia == null,
                                    enter = fadeIn(animationSpec = tween(1000 + (index.times(1000)))),
                                    exit = fadeOut(animationSpec = tween(1000)),
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .height(80.dp)
                                            .clickable(enabled = true, onClick = { backStack.add(ProfileTabs.MEDIA); previewMedia = media }),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(media?.uri?.toUri()),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize().padding(10.dp) ,contentAlignment = Alignment.BottomEnd) {
                    Button(onClick = {
                        mediaPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) {
                        Icon(painter = painterResource(R.drawable.outline_add_24), contentDescription = "Add Photo")
                    }
                }
            }
        }
    )
}