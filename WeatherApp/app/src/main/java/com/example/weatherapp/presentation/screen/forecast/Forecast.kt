package com.example.weatherapp.presentation.screen.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.dto.forcastmodel.ForecastItem
import com.example.weatherapp.presentation.screen.forecast.components.ForecastCard


@Composable
fun ForecastSection(forecast: List<ForecastItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)){
        Text(
            text = "24-Hours Forecast",
            color = Color.White,
           style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(forecast){ item ->
                ForecastCard(item)
            }
        }

    }

}