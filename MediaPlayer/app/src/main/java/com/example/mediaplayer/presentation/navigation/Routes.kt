package com.example.mediaplayer.presentation.navigation


import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object home : Routes()
    @Serializable
    data object LocalVedios : Routes()
    @Serializable
    data class LocalPlayer(val vedioId: Long) : Routes()
    @Serializable
    data object WebPlayer : Routes()
}