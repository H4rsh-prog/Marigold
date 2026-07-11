package com.example.marigold.composables.activity_dashboard

import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.marigold.MainActivity
import com.example.marigold.services.DataHandler

@Composable
fun DashboardHome(modifier: Modifier = Modifier) {
    val context = LocalContext.current;
    val dataHandler = DataHandler(context)
    HomeScreen(resolveView = {
        dataHandler.removePreference(dataHandler.DEFINE_MARIGOLD)
        Toast.makeText(context, "You get to redefine your marigold", Toast.LENGTH_SHORT).show();
        context.startActivity(Intent(context, MainActivity::class.java));
    })
}