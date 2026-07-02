package com.example.marigold.composables.activity_dashboard

import android.content.Intent
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.marigold.MainActivity

@Composable
fun DashboardHome(modifier: Modifier = Modifier) {
    Reboot();
}

@Composable
fun Reboot(modifier: Modifier = Modifier) {
    val context = LocalContext.current;
    ElevatedButton(onClick = {
        val intent = Intent(context, MainActivity::class.java);
        context.startActivity(intent);
    }) {
        Text("Reboot")
    }
}