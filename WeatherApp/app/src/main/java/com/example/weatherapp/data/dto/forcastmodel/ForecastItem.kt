package com.example.weatherapp.data.dto.forcastmodel

import kotlinx.serialization.Serializable

@Serializable
data class ForecastItem(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val pop: Double = 0.0,
    val rain: Rain? = null,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)
