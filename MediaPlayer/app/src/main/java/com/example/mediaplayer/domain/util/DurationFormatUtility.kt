package com.example.mediaplayer.domain.util

/**
 * Formats duration from milliseconds to human-readable time string
 * @param millis Duration in milliseconds
 * @return Formatted string like "1:23:45" or "12:34"
 */
fun FormatDuration(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60 // Extract minutes (0-59)
    val hours = (millis / (1000 * 60 * 60))

    return if (hours > 0) { // If video is 1 hour or longer
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else { // If video is less than 1 hour
        String.format("%d:%02d", minutes, seconds)
    }

}


/**
 * Formats file size from bytes to human-readable string
 * @param bytes File size in bytes
 * @return Formatted string like "1.23 GB", "456.78 MB", or "12.34 KB"
 */

fun FormatFileSize(bytes: Long): String {

    val kb = bytes / 1024.0 //Convert to kilobytes
    val mb = kb / 1024.0
    val gb = mb / 1024.0

    return when {

        // Choose appropriate unit

        gb >= 1 -> String.format("%.2f GB", gb)
        mb >= 1 -> String.format("%.2f MB", mb)
        else -> String.format("%.2f KB", kb)

    }


}