//package com.example.marigold.composables.DashboardComposables
//
//import android.widget.Toast
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import com.example.marigold.composables.NavigationIndx
//import com.example.marigold.services.DataHandler
//
//@Composable
//fun DashboardHome(modifier: Modifier = Modifier, overrideNavigationIndx : (NavigationIndx) -> Unit) {
//    val context = LocalContext.current;
//    val dataHandler = DataHandler(context)
//    HomeScreen(resolveView = {
//        Toast.makeText(context, "You get to redefine your marigold", Toast.LENGTH_SHORT).show();
//        overrideNavigationIndx(NavigationIndx.SPLASH_SCREEN)
//    })
//}