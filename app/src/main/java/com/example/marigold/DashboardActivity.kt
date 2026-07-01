package com.example.marigold

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults.colors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
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

//@PreviewScreenSizes
@Composable
fun Dashboard(overrideDestination: AppDestinations = AppDestinations.HOME, modifier : Modifier = Modifier) {
    var currentDestination by rememberSaveable { mutableStateOf(overrideDestination) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            painterResource(it.icon),
                            contentDescription = it.label
                        )
                    },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it },
                    modifier = modifier.alpha(0.6f)
                )
            }
        },
        layoutType = NavigationSuiteType.NavigationBar,
        navigationSuiteColors = colors(
            navigationBarContainerColor = MaterialTheme.colorScheme.background
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            when (currentDestination) {
                AppDestinations.FAVORITES -> DashboardFavorites()
                AppDestinations.HOME -> DashboardHome()
                AppDestinations.PROFILE -> DashboardProfile()
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    FAVORITES("Favorites", R.drawable.ic_favorite),
    HOME("Home", R.drawable.ic_home),
    PROFILE("Profile", R.drawable.ic_account_box)
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
@Composable
fun DashboardHome(modifier: Modifier = Modifier) {
    Reboot();
}
@Composable
fun DashboardFavorites(modifier: Modifier = Modifier) {
    Reboot();
}
@Composable
fun DashboardProfile(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        items(listOf("Phrases", "Calender", "Media", "Memories")) {
            Card(
                elevation = CardDefaults.elevatedCardElevation(5.dp),
                border = CardDefaults.outlinedCardBorder(true),
                shape = CardDefaults.elevatedShape,
                modifier = modifier.padding(15.dp)
                    .fillMaxWidth()
            ){
                Text(it, modifier = modifier.padding(5.dp))
            }
        }
    }
}



@PreviewLightDark
@Composable
fun GreetingPreview2() {
    MarigoldTheme {
        Dashboard(overrideDestination = AppDestinations.PROFILE)
    }
}