package com.example.weatherapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.dto.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(val repository: WeatherRepository = WeatherRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)

    //    val uiState: StateFlow<WeatherUiState> = _uiState
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("Lahore")
    val searchQuery = _searchQuery.asStateFlow()


    init {
        loadWeather("Lahore")
    }

    fun loadWeather(city: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            val weatherResult = repository.getCurrentWeather(city)

            weatherResult.fold(
                onSuccess = { weather ->
                    val forecastResult = repository.getForecast(city)
                    Log.d("forecast", forecastResult.toString())
                    forecastResult.fold(
                        onSuccess = { forecasts ->
                            _uiState.value = WeatherUiState.Success(
                                currentWeather = weather,
                                forecast = forecasts.list.take(8)
                            )
                        },
                        onFailure = {
                            _uiState.value = WeatherUiState.Success(
                                currentWeather = weather,
                                forecast = emptyList()
                            )
                        }
                    )
                },
                onFailure = { error ->
                    _uiState.value = WeatherUiState.Error(error.message ?: "Failed to load weather")
                }
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun refresh() {
        loadWeather(_searchQuery.value)
    }

    override fun onCleared() {
        super.onCleared()
        repository.close()
    }
}
