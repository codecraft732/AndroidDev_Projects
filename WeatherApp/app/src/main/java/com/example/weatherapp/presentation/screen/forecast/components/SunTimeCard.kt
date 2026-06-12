package com.example.weatherapp.presentation.screen.forecast.components


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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SunTimeCard(weather: WeatherModel, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


    val responsivePadding = screenWidth * 0.06f // 6% of screen width
    val labelSize = (screenHeight.value * 0.018).sp // Dynamic label size
    val timeSize = (screenHeight.value * 0.022).sp  // Dynamic time size
    val iconSize = (screenHeight.value * 0.035).sp  // Dynamic icon size

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(responsivePadding),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sunrise Column
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🌅",
                    fontSize = iconSize
                )
                Spacer(modifier = Modifier.height(screenWidth * 0.02f))
                Text(
                    text = "Sunrise",
                    color = Color.White.copy(0.7f),
                    fontSize = labelSize,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = getTimeFromTimestamp(weather.sys.sunrise.toLong()),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = timeSize,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Sunset Column
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🌇",
                    fontSize = iconSize
                )
                Spacer(modifier = Modifier.height(screenWidth * 0.02f))
                Text(
                    text = "Sunset",
                    color = Color.White.copy(0.7f),
                    fontSize = labelSize,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = getTimeFromTimestamp(weather.sys.sunset.toLong()),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = timeSize,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

fun getTimeFromTimestamp(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(date)
}
