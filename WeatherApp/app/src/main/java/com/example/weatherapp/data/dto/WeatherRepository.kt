package com.example.weatherapp.data.dto

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.dto.forcastmodel.ForecastModel
import com.example.weatherapp.data.dto.getcurrentweather.WeatherModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Singleton
class WeatherRepository( private val client : HttpClient){

    private val API_KEY = BuildConfig.WeatherAPI_KEY
    private val BASE_URL = BuildConfig.WEATHER_BASE_URL

    //suspend = this function run in coroutine (background thread)
    suspend fun getCurrentWeather(city: String): Result<WeatherModel> { //Result<WeatherModel> = Success Failure return

        return try {
            val response = client.get("$BASE_URL/weather") {
                parameter("q", city)
                parameter("appid", API_KEY)
                parameter("units", "metric")
            }
            Result.success(response.body()) ////  convert JSON to WeatherModel dataclass object

        } catch (e: Exception) {
            Result.failure(e)
        }


    }

    suspend fun getForecast(city: String): Result<ForecastModel> {
        return try {
            val response = client.get("$BASE_URL/forecast") {
                parameter("q", city)
                parameter("appid", API_KEY)
                parameter("units", "metric")
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun close() {
        client.close()
    }
}