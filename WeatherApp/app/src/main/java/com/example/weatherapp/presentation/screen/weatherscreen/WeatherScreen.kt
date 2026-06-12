package com.example.weatherapp.presentation.screen.weatherscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import com.example.weatherapp.presentation.*
import com.example.weatherapp.presentation.screen.weather.WeatherContent
import com.example.weatherapp.presentation.screen.weatherscreen.component.*

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var showSearchDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3A8A),
                        Color(0xFF3882F6),
                        Color(0xFF60A5FA)
                    )
                )
            )
    ) {
        when (val state = uiState) {
            is WeatherUiState.Loading -> {
                LoadingScreen()
            }

            is WeatherUiState.Success -> {
                WeatherContent(
                    weather = state.currentWeather,
                    forecast = state.forecast,
                    onSearchClick = { showSearchDialog = true }
                )
            }

            is WeatherUiState.Error -> {
                ErrorScreen(message = state.message) {
                    viewModel.refresh()
                }
            }
        }

        if (showSearchDialog) {
            ShowDialogue(
                currentQuery = searchQuery,
                onDismiss = { showSearchDialog = false },
                onSearch = { city ->
                    viewModel.updateSearchQuery(city)
                    viewModel.loadWeather(city)
                    showSearchDialog = false
                }
            )
        }
    }
}
