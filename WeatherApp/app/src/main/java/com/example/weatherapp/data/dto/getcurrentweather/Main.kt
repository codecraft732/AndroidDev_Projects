package com.example.weatherapp.data.dto.getcurrentweather

import kotlinx.serialization.Serializable

@Serializable
data class Main(
    val feels_like: Double,
    val grnd_level: Int = 0,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int = 0,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double
)
