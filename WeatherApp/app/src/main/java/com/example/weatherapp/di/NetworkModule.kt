package com.example.weatherapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent ::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient{
        // http send network request
        return HttpClient(CIO) {
//       ContentNegotiation  convert api response from Json to object
            install(ContentNegotiation) {

                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                }
                )
            }

            install(Logging) {
                logger = Logger.Companion.DEFAULT
                level = LogLevel.INFO  //  show URL, headers, status code
            }
        }

    }
}