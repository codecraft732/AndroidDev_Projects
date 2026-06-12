package com.example.weatherapp.presentation.screen.weatherscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.weatherapp.presentation.WeatherUiState
import com.example.weatherapp.presentation.WeatherViewModel
import com.example.weatherapp.presentation.screen.weather.WeatherContent
import com.example.weatherapp.presentation.screen.weatherscreen.component.ErrorScreen
import com.example.weatherapp.presentation.screen.weatherscreen.component.LoadingScreen
import com.example.weatherapp.presentation.screen.weatherscreen.component.ShowDialogue

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
