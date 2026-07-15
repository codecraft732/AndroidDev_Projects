package com.example.mediaplayer.presentation

import android.os.Build
import android.view.ViewGroup
import android.webkit.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController

/**
 * Web Player Screen - Displays YouTube video in WebView
 * Loads a specific YouTube URL directly in the browser
 * @param navController Navigation controller for back navigation
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebPlayerScreen(navController: NavHostController) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Web Video Player") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {


            // AndroidView embeds native Android View in Compose
            AndroidView(
                factory = { ctx -> // Factory creates the view once
                    WebView(ctx).apply { // Create WebView
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, // Full width
                            ViewGroup.LayoutParams.MATCH_PARENT // Full height
                        )

                        // Enable hardware acceleration for better video performance
                        setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)

                        // Configure WebView settings
                        settings.apply {
                            javaScriptEnabled = true // Enable JavaScript (required for YouTube)
                            javaScriptCanOpenWindowsAutomatically = true // Allow popups
                            loadWithOverviewMode = true // Load page in overview mode
                            useWideViewPort = true // Enable viewport meta tag
                            domStorageEnabled = true // Enable DOM storage (required for YouTube)
                            databaseEnabled = true // Enable database storage
                            mediaPlaybackRequiresUserGesture = false // Allow autoplay
                            allowFileAccess = true // Allow file access
                            allowContentAccess = true // Allow content access
                            setSupportZoom(true) // Enable zoom
                            builtInZoomControls = true // Show zoom controls
                            displayZoomControls = false // Hide zoom button overlay

                            // Allow mixed HTTP/HTTPS content (Android 5.0+)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            }
                        }

                        webChromeClient = WebChromeClient() // Handle JavaScript dialogs, etc.
                        webViewClient = WebViewClient() // Handle page navigation

                        // Load the specific YouTube video URL
                        //  includes video ID and timestamp (t=83s starts at 83 seconds)
                        loadUrl("https://youtu.be/VMj-3S1tku0?si=y13UsxXDFUZu6VbA")

                    }
                },

                modifier = Modifier.fillMaxSize() // WebView fills entire screen
            )

        }
    }
}
