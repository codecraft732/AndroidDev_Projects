package com.example.mediaplayer.presentation.localvedioscreen

import android.app.Activity
import android.app.RecoverableSecurityException
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mediaplayer.models.LocalVedio
import com.example.mediaplayer.presentation.navigation.Routes
import com.example.mediaplayer.presentation.localvedioscreen.component.VedioItems
import com.example.mediaplayer.presentation.viewmodel.VedioViewModel
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalVediosScreen(
    navController: NavHostController,
    viewModel: VedioViewModel
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { // unit so it won't load again and again or degrade the performance
        scope.launch {
            viewModel.loadlocalVedios()
        }
    }




    var videoToDelete by remember { mutableStateOf<LocalVedio?>(null) }
    var pendingDeleteVideo by remember { mutableStateOf<LocalVedio?>(null) }


    val deleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            pendingDeleteVideo?.let { video ->
                scope.launch {
                    viewModel.removeVideoFromList(video)
                }
                Toast.makeText(context, "Video deleted!", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(context, "Delete cancelled", Toast.LENGTH_SHORT).show()
        }

        pendingDeleteVideo = null
    }






    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Local Vedios") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->


        if (viewModel.isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }


        } else if (viewModel.localVedios.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Icon( imageVector = Icons.Default.VideoLibrary,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )

                    Text(
                        text = "No videos found",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.outline
                    )

                }
            }
        }


        else{

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(viewModel.localVedios){ vedioRef ->
                    VedioItems(
                        video = vedioRef,
                        onClick={
                            navController.navigate(Routes.LocalPlayer(vedioId = vedioRef.id))
                        },
                        onDelete = { videoToDelete = vedioRef }

                    )

                }
            }
        }
    }


    videoToDelete?.let { video ->

        AlertDialog(
            onDismissRequest = { videoToDelete = null },
            title = { Text(text = "Delete Video") },
            text = {
                Text(text = "Are you sure you want to permanently delete \"${video.title}\"?\n\nThis action cannot be undone!")
            },
            confirmButton = {
                TextButton(onClick = {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                        try {
                            val deleteRequest = MediaStore.createDeleteRequest(
                                context.contentResolver,
                                listOf(video.uri)
                            )
                            pendingDeleteVideo = video
                            deleteLauncher.launch(
                                IntentSenderRequest.Builder(deleteRequest.intentSender).build()
                            )
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Delete failed: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {

                        scope.launch {
                            try {
                                val deleted = withContext(Dispatchers.IO) {
                                    context.contentResolver.delete(video.uri, null, null) > 0
                                }

                                if (deleted) {
                                    viewModel.removeVideoFromList(video)
                                    Toast.makeText(context, "Video deleted", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            } catch (securityException: SecurityException) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    val recoverableException =
                                        securityException as? RecoverableSecurityException

                                    recoverableException?.let {
                                        pendingDeleteVideo = video
                                        deleteLauncher.launch(
                                            IntentSenderRequest.Builder(it.userAction.actionIntent.intentSender)
                                                .build()
                                        )
                                    } ?: run {
                                        Toast.makeText(
                                            context,
                                            "Cannot delete this video",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Cannot delete this video",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }

                }) {
                    Text(text = "Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { videoToDelete = null }) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}
