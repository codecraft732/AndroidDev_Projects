package com.example.weatherapp.data.dto.forcastmodel

import kotlinx.serialization.Serializable

@Serializable
data class Rain(
    val `3h`: Double
)