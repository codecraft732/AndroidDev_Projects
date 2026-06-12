package com.example.weatherapp.presentation.screen.weather.component


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.weatherapp.data.dto.getcurrentweather.WeatherModel

@Composable
fun MainWeatherCard(weather: WeatherModel) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp


    val cardHeight = screenHeight * 0.35f           // 35% of screen height
    val responsivePadding = screenWidth * 0.08f      // 8% of screen width
    val emojiSize = (screenHeight.value * 0.08).sp // 8% of screen height value as font size
    val tempSize = (screenHeight.value * 0.09).sp  // 9% of screen height value as font size

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.15f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(responsivePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Weather Emoji
            Text(
                text = getWeatherEmoji(weather.weather.firstOrNull()?.main ?: "clear"),
                fontSize = emojiSize
            )

            Spacer(modifier = Modifier.height(verticalArrangementSpacing(screenHeight)))

            // Temperature
            Text(
                text = "${weather.main.temp.toInt()}°",
                color = Color.White,
                fontSize = tempSize,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = weather.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() }
                    ?: "",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

private fun verticalArrangementSpacing(screenHeight: Dp): Dp {
    return screenHeight * 0.02f
}

fun getWeatherEmoji(condition: String): String {
    return when (condition.lowercase()) {
        "clear" -> "☀️"
        "clouds" -> "☁️"
        "rain" -> "🌧️"
        "drizzle" -> "🌦️"
        "thunderstorm" -> "⛈️"
        "snow" -> "❄️"
        "mist", "fog", "haze" -> "🌫️"
        else -> "⛅"
    }
}
