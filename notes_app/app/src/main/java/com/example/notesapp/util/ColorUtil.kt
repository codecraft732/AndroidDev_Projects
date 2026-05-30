package com.example.notesapp.util

object ColorUtil {

    private val colors = listOf(
        0xFF7D5260,
        0xFF625b71,
        0xFF6650a4,
        0xFFEFB8C8,
        0xFFD0BCFF,
        0xFFCCC2DC,
        0xFFD552A3,
        0xFF547A95,
        0xFF56B6C6,
        0xFF9F9F9F,
        0xFF9F9F9F,
        0xFFFF00FF,
        0xFF800080,
        0xFF00FFFF,
        0xFFC0C0C0,
        0xFFE9967A
    )

    fun getRandomColor(): Long{
        return colors.random()
    }
}