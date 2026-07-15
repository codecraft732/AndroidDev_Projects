package com.example.mediaplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mediaplayer.domain.repository.VedioRepository
import com.example.mediaplayer.presentation.navigation.NavGraph
import com.example.mediaplayer.presentation.viewmodel.VedioViewModel
import com.example.mediaplayer.ui.theme.MediaPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaPlayerTheme {
            val repository = VedioRepository(applicationContext)
                val viewModel : VedioViewModel = viewModel {
                    VedioViewModel(repository)
                }
                NavGraph(viewModel)
            }
        }
    }
}
