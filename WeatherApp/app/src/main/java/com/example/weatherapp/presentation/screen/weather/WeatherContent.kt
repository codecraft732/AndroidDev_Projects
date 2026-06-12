package com.example.weatherapp.presentation.screen.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.dto.forcastmodel.*
import com.example.weatherapp.data.dto.getcurrentweather.WeatherModel
import com.example.weatherapp.presentation.screen.forecast.components.SunTimeCard
import com.example.weatherapp.presentation.screen.forecast.ForecastSection
import com.example.weatherapp.presentation.screen.weather.component.MainWeatherCard
import com.example.weatherapp.presentation.screen.weather.component.WeatherDetailGrid
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun WeatherContent(
    weather: WeatherModel,
    forecast: List<ForecastItem>,
    onSearchClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val iconSize = screenWidth * 0.12f // Responsive icon button size
    
    // Responsive spacing between sections (3% of screen height)
    val sectionSpacing = screenHeight * 0.03f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(sectionSpacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = weather.name,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = weather.sys.country,
                    color = Color.White.copy(0.7f),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .background(
                        color = Color.White.copy(0.2f)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }
        
        Text(
            text = getCurrentDate(),
            color = Color.White.copy(0.8f),
            style = MaterialTheme.typography.titleMedium
        )

        MainWeatherCard(weather)

        WeatherDetailGrid(weather)

        if(forecast.isNotEmpty()){
            ForecastSection(forecast)
        }

        SunTimeCard(weather)
        
        // Extra padding at bottom for better scrolling experience
        Spacer(modifier = Modifier.height(16.dp))
    }
}


fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("EEE, MMMM dd, YYYY", Locale.getDefault())
    return sdf.format(Date())
}
