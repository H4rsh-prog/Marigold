package com.example.marigold.composables.DashboardComposables.ProfileTabsComposables

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import com.example.marigold.model.DB
import com.example.marigold.model.Media.Media
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MediaComposable(revertProfile: () -> Unit, backStack: SnapshotStateList<Any>) {
    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(
            context = context,
            klass = DB::class.java,
            name = DB.DB_NAME
        ).createFromAsset("databases/initMarigold.db").build()
    }
    val dao = remember { db.mediaDAO() }
    val scope = rememberCoroutineScope()
    var mediaItems by remember { mutableStateOf(null as List<Media>?) }
    var loaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        mediaItems = dao.getAll().sortedByDescending { it.date }
        delay(200)
        loaded = true
    }
    val mediaPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<android.net.Uri> ->
        if (uris.isNotEmpty()) {
            scope.launch {
                for (uri in uris) {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    dao.upsert(Media(uri = uri.toString()))
                }
                mediaItems = dao.getAll().sortedByDescending { it.date }
            }
        }
    }
    var previewMedia by remember { mutableStateOf(null as Media?) }
    AnimatedVisibility(
        visible = loaded,
        enter = fadeIn() + scaleIn(initialScale = 0.8f),
        exit = fadeOut() + scaleOut(targetScale = 0.8f)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Gallery of Moments",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${mediaItems?.size ?: 0} memories captured",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    var itemsLoaded by mutableStateOf(false)
                    scope.launch {
                        delay(300)
                        itemsLoaded = true
                    }
                    mediaItems?.let { items ->
                        itemsIndexed(items) { index, media ->
                            AnimatedVisibility(
                                visible = itemsLoaded,
                                enter = scaleIn(animationSpec = tween(600, index * 80)) + fadeIn()
                            ) {
                                MediaThumbnail(
                                    media = media,
                                    onClick = { previewMedia = media }
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(8.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        onClick = {
                            mediaPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.tertiary
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = Color.White)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    "CAPTURE A MOMENT",
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = previewMedia != null,
                enter = fadeIn() + scaleIn(initialScale = 0.9f),
                exit = fadeOut() + scaleOut(targetScale = 0.9f)
            ) {
                previewMedia?.let { media ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(media.uri.toUri()),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(20.dp),
                            contentScale = ContentScale.Crop,
                            alpha = 1f
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(media.uri.toUri()),
                                contentDescription = null,
                                modifier = Modifier.clip(RoundedCornerShape(32.dp)).border(BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { previewMedia = null },
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                                }
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            dao.deleteById(media.id)
                                            mediaItems = dao.getAll().sortedByDescending { it.date }
                                            previewMedia = null
                                        }
                                    },
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.2f), CircleShape)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaThumbnail(media: Media, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .shadow(4.dp, RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f))
    ) {
        Image(
            painter = rememberAsyncImagePainter(media.uri.toUri()),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
