package com.example.weatherapp.presentation.screen.weather.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.dto.getcurrentweather.WeatherModel

@Composable
fun WeatherDetailGrid(weather: WeatherModel) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Responsive spacing
    val verticalSpacing = screenHeight * 0.02f
    val horizontalSpacing = screenWidth * 0.03f

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        // First Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
        ) {
            WeatherDetailCard(
                icon = "💨",
                label = "Wind Speed",
                value = "${weather.wind.speed} m/s",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailCard(
                icon = "💧",
                label = "Humidity",
                value = "${weather.main.humidity}%",
                modifier = Modifier.weight(1f)
            )
        }

        // Second Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
        ) {
            WeatherDetailCard(
                icon = "🌡️",
                label = "Pressure",
                value = "${weather.main.pressure} hPa",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailCard(
                icon = "👁️",
                label = "Visibility",
                value = "${weather.visibility / 1000} km",
                modifier = Modifier.weight(1f)
            )
        }
    }
}
