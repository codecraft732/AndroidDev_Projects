package com.example.weatherapp.presentation.screen.forecast.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.dto.forcastmodel.ForecastItem
import com.example.weatherapp.presentation.screen.weather.component.getWeatherEmoji

@Composable
fun ForecastCard(forecast: ForecastItem) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


    val cardWidth = screenWidth * 0.22f            // 22% of screen width
    val cardHeight = screenHeight * 0.16f           // 16% of screen height
    val timeFontSize = (screenHeight.value * 0.015).sp // ~1.5% of height
    val emojiFontSize = (screenHeight.value * 0.035).sp // ~3.5% of height
    val tempFontSize = (screenHeight.value * 0.02).sp   // ~2% of height

    Card(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Time
            Text(
                text = getTimeFromTimestamp(forecast.dt.toLong()),
                color = Color.White.copy(0.8f),
                fontSize = timeFontSize,
                fontWeight = FontWeight.Medium
            )

            // Weather Emoji
            Text(
                text = getWeatherEmoji(forecast.weather.firstOrNull()?.main ?: "clear"),
                fontSize = emojiFontSize
            )

            // Temperature
            Text(
                text = "${forecast.main.temp.toInt()}°",
                color = Color.White,
                fontSize = tempFontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
