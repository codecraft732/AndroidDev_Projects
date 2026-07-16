package com.example.mediaplayer.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mediaplayer.models.LocalVedio
import com.example.mediaplayer.domain.repository.VedioRepository

/**
ViewModel for managing video data and UI state
Survives configuration changes (like screen rotation)
@param repository VideoRepository instance for data access
 */
class VedioViewModel(private val repository: VedioRepository): ViewModel() {


    var localVedios by  mutableStateOf<List<LocalVedio>>(emptyList())
        private set  // Only this class can modify the list


    var isLoading by mutableStateOf(false)
        private set  // Only this class can modify loading state



    suspend fun loadlocalVedios(){
        isLoading = true
        localVedios = repository.getLocalVedio()
        isLoading = false
    }


    fun getVideoUri(id: Long): Uri {
        return repository.getVediobyId(id)
    }




    suspend fun deleteVideo(video: LocalVedio): Boolean {
        val success = repository.deleteVideo(video.uri)//video.uri, the address of the file

        if (success) {
            localVedios= localVedios.filter { it.id != video.id }//.filter { it.id != video.id } → "keep every video EXCEPT the one whose id matches the deleted one"
        }
        return success
    }



    fun removeVideoFromList(video: LocalVedio) {
        localVedios = localVedios.filter { it.id != video.id }
    }
}