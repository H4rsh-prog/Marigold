package com.example.marigold.composables.DashboardComposables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.marigold.composables.DashboardComposables.ProfileTabsComposables.MediaComposable
import com.example.marigold.composables.DashboardComposables.ProfileTabsComposables.MemoriesComposable
import com.example.marigold.composables.DashboardComposables.ProfileTabsComposables.NotesComposable

enum class ProfileTabs(
    val label: String,
    val content: @Composable (revertProfile: () -> Unit, backStack: SnapshotStateList<Any>) -> Unit,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    NOTES("Notes", {revertProfile, backStack -> NotesComposable(revertProfile, backStack)}),
    MEDIA("Media", {revertProfile, backStack -> MediaComposable(revertProfile, backStack)}),
    MEMORIES("Memories", { revertProfile, backStack -> MemoriesComposable(revertProfile, backStack)})
}