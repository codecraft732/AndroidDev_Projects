package com.example.weatherapp.presentation.screen.forecast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.dto.forcastmodel.ForecastItem
import com.example.weatherapp.presentation.screen.forecast.components.ForecastCard


@Composable
fun ForecastSection(forecast: List<ForecastItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "24-Hours Forecast",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(forecast) { item ->
                ForecastCard(item)
            }
        }

    }

}