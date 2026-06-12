package com.example.weatherapp.data.dto.forcastmodel

import kotlinx.serialization.Serializable

@Serializable
data class ForecastModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastItem>,
    val message: Int
)