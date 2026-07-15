package com.example.mediaplayer.domain.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.mediaplayer.models.LocalVedio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


// context means a reference for a file or func ..."File context" = path, folder, or project the file is in.
class VedioRepository(val context: Context) {

    /**Retrieves all video files from device storage
     @return List of LocalVideo objects*/
    suspend fun getLocalVedio(): List<LocalVedio> = withContext(Dispatchers.IO)
    {
        val vedios = mutableListOf<LocalVedio>()

        /** Define which columns to retrieve from MediaStore**/
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.MIME_TYPE
        )

        /** Sort videos by date added, newest first**/
        val sortOrder =" ${MediaStore.Video.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val sizeColumn= cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val nameColumn= cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn= cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val dateColumn= cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
            val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)

            while(cursor.moveToNext()){
                val name = cursor.getString(nameColumn)
                val id = cursor.getLong(idColumn)
                val size = cursor.getLong(sizeColumn)
                val duration = cursor.getLong(durationColumn)
                val date= cursor.getString(dateColumn)
                val mimeType = cursor.getString(mimeColumn)

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                vedios.add(
                    LocalVedio(
                    id = id,
                    title = name ,
                    uri = contentUri,
                    size = size,
                    duration = duration,
                    dateAdded = date,
                    mimeType = mimeType
                    )
                )
            }

        }

        vedios
    }

    /**
     Retrieves a video's content URI by its ID
     @param id Video ID from MediaStore
     @return Content URI for the video
     */

    fun getVediobyId(id: Long) :Uri{
        return ContentUris.withAppendedId(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            id
        )

    }


    suspend fun deleteVideo(uri: Uri): Boolean = withContext(Dispatchers.IO) {

        try {
            val rowsDeleted = context.contentResolver.delete(uri,  null, null)
            rowsDeleted > 0
        } catch (e: Exception) {
            false
        }

    }
}