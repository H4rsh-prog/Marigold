package com.example.marigold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Density
import com.example.marigold.composables.AppNavigation
import com.example.marigold.ui.theme.MarigoldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarigoldTheme {
                val density = LocalDensity.current
                val fontScale = minOf(density.fontScale, 1.3f)
                CompositionLocalProvider(
                    LocalDensity provides Density(
                        density = 1.85f,
                        fontScale = 1.3f
                    )
                ) {
                    AppNavigation(
//                        onAuthenticate = BiometricFingerprintAuthentication()::showBiometricPrompt,
                        viewIndx = -1
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun GreetingPreview() {
    MarigoldTheme {
        AppNavigation(0)
    }
}
