package com.example.mediaplayer.models

import android.net.Uri
import java.net.URI

data class LocalVedio(
    val id: Long,
    val title: String,
    val uri: Uri, // Content URI to access the video file
    val size: Long, // File size in bytes
    val duration: Long,
    val dateAdded: String?,
    val thumbnailUri: URI? = null,// Optional thumbnail URI
    val mimeType: String? = null // MIME type (e.g., "video/mp4")
)
