package com.example.mediaplayer.presentation.localvedioscreen.component

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mediaplayer.models.LocalVedio
import com.example.mediaplayer.domain.util.*
import kotlinx.coroutines.*


/**
 * Composable function that displays a single video item in the list
 * Generates and displays video thumbnail automatically
 * @param video LocalVideo object containing video information
 * @param onClick Callback function when video is clicked
 */

@Composable
fun VedioItems(
    modifier: Modifier = Modifier,
    video: LocalVedio,
    onDelete: () -> Unit,
    onClick: () -> Unit) {


    val context = LocalContext.current
    var thumbnail by remember { mutableStateOf<Bitmap?>(null) }  // State to hold thumbnail bitmap



    LaunchedEffect(video.uri){
       withContext(Dispatchers.IO) {
           try{
               // MediaMetadataRetriever extracts frames from video
               val retriever = MediaMetadataRetriever()
               retriever.setDataSource(context, video.uri) // Set video source
               thumbnail = retriever.getFrameAtTime(1000000)// Get frame at 1 second (in microseconds)
               retriever.release() // Release resources

           }
           catch (e: Exception) {

               e.printStackTrace() // Log any errors
           }

        }

    }


    // Card container for video item
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Make entire card clickable
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {


        // Row arranges thumbnail and info horizontally
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp), // Padding inside card
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            // Thumbnail container
            Box(
                modifier = Modifier
                    .size(80.dp, 60.dp) // Fixed size for thumbnail
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center // Center content
            ) {

                if (thumbnail != null) { // If thumbnail loaded successfully
                    Image(
                        bitmap = thumbnail!!.asImageBitmap(), // Convert Bitmap to ImageBitmap
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                    else { // While loading or if failed
                        Icon(
                            imageVector = Icons.Default.PlayCircle, // Play icon placeholder
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }




            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
                Text(
                    text = FormatDuration(video.duration),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = FormatFileSize(video.size),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )

            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.primary
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
