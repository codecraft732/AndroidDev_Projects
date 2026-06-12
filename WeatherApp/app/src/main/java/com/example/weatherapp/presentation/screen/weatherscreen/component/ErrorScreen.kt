package com.example.weatherapp.presentation.screen.weatherscreen.component


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // Responsive Dimensions
    val iconSize = (screenHeight.value * 0.08).sp    // 8% of screen height
    val titleSize = (screenHeight.value * 0.035).sp  // 3.5% of screen height
    val messageSize = (screenHeight.value * 0.02).sp // 2% of screen height
    val spacing = screenHeight * 0.02f               // 2% of screen height
    val padding = screenWidth * 0.1f                 // 10% of screen width

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier.padding(padding)
        ) {
            Text(
                text = "⚠",
                fontSize = iconSize
            )

            Text(
                text = "Oops!",
                color = Color.White,
                fontSize = titleSize,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = message,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = messageSize,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1E3A8A)
                ),
                modifier = Modifier.padding(top = spacing)
            ) {
                Text(
                    text = "Try Again",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = messageSize
                )
            }
        }
    }
}
