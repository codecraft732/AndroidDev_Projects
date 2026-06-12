package com.example.weatherapp.presentation

import com.example.weatherapp.data.dto.forcastmodel.ForecastItem
import com.example.weatherapp.data.dto.getcurrentweather.WeatherModel

sealed class WeatherUiState {

    //object so singelten avoid Memory leak (if obj already exist don't create object again & again)
    object Loading : WeatherUiState()

    data class Success(
        val currentWeather: WeatherModel,
        val forecast: List<ForecastItem> = emptyList()
    ) : WeatherUiState()

    data class Error(val message: String) : WeatherUiState()

}
