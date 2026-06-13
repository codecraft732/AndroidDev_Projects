package com.example.weatherapp.di

import com.example.weatherapp.data.dto.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideWeatherRepository(httpClient: HttpClient): WeatherRepository {
        return WeatherRepository(httpClient)
    }
}