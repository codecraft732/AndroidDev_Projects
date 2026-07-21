package com.example.doc_scanner

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.doc_scanner.ui.theme.Doc_ScannerTheme
import com.google.mlkit.vision.documentscanner.*
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.*
import kotlinx.coroutines.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * MAIN SCREEN OF THE APP
 *
 * Flow (what happens step by step):
 * 1. User taps "Scan Document"
 * 2. We check if CAMERA permission is granted. If not, we ASK for it (runtime dialog).
 * 3. Only after permission is granted do we open Google's scanner UI.
 * 4. User finishes scanning -> we get image URIs + a PDF back.
 * 5. We save both to permanent storage ON A BACKGROUND THREAD (not main thread — avoids freezing/crashing).
 * 6. We show a Toast ONLY after the save actually finishes, success or failure.
 */
class MainActivity : ComponentActivity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val options =
            GmsDocumentScannerOptions.Builder() //start building the scanner's settings step by step.
                .setScannerMode(SCANNER_MODE_FULL) //  Use full scanner mode (all features enabled).
                .setGalleryImportAllowed(true)     // Allow the user to pick an existing photo from their gallery, not just camera.
                .setPageLimit(5)  // Maximum of 5 pages can be scanned at once.
                .setResultFormats(
                    RESULT_FORMAT_JPEG,
                    RESULT_FORMAT_PDF
                ) // Ask for BOTH jpeg images AND a combined pdf as output.
                .build() // finish building and give us the final "options" object.


        val scanner =
            GmsDocumentScanning.getClient(options)  // Create the actual scanner tool ("client") using the settings ("options") we just built.



        setContent {
            Doc_ScannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    // "var imageUris" = a variable that CAN change over time.
                    // "by remember { mutableStateOf(...) }" = make it a special state variable that:
                    //   1) survives UI redraws (remember)
                    //   2) automatically updates the screen whenever it changes (mutableStateOf)
                    // "List<Uri>" = a list containing image addresses (Uri).
                    // "emptyList()" = starts as an empty list (no images yet).

                    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }



                    // Fires after the scanner activity closes and hands back results
                    val scannerLauncher = rememberLauncherForActivityResult(

                        // contract = tells Android WHAT KIND of action we are launching
                        // (here: launching an intent sender, needed for the scanner).
                        contract = ActivityResultContracts.StartIntentSenderForResult(),

                        // onResult = this code block runs AFTER the scanner finishes and returns data.
                        // "activityResult" = the data/result coming back from the scanner.
                        onResult = { activityResult: ActivityResult ->

                            // Only continue if the scan was successful (RESULT_OK = success code).
                            if (activityResult.resultCode == Activity.RESULT_OK) {

                                // Convert the raw returned data into a proper, readable scanning result object.
                                val result = GmsDocumentScanningResult.fromActivityResultIntent(
                                    activityResult.data
                                )

                                // "result?.pages" = safely get the list of scanned pages (if result isn't null).
                                // ".map { it.imageUri }" = from each page, extract just its image address (Uri).
                                // "?: emptyList()" = if anything was null, use an empty list instead of crashing.
                                // Save the final list into imageUris — this automatically updates the screen!
                                val scannedPages = result?.pages?.map { it.imageUri } ?: emptyList()
                                imageUris = scannedPages

                                // Save happens in the background — never on the main thread
                                activityScope.launch {
                                    saveScanResults(scannedPages, result?.pdf?.uri)
                                }
                            }
                        }
                    )

                    // Permissions
                    val cameraPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            if (isGranted) {
                                launchScanner(scanner, scannerLauncher)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Camera permission is required to scan documents",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    )

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text= "Welcome to the Doc Scanner",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight= FontWeight.Bold,
                            fontFamily = FontFamily.Cursive
                        )


                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Button(onClick = {
                                // Check permission FIRST. Only open the scanner if we
                                // already have it — otherwise ask for it and stop here.
                                val hasCameraPermission = ContextCompat.checkSelfPermission(
                                    this@MainActivity,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasCameraPermission) {
                                    launchScanner(scanner, scannerLauncher)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                                },
                                modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 16.dp,horizontal= 24.dp)
                                .background(
                                    color = Color.Black,
                                    shape = RectangleShape
                                )
                               , colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black,
                                    disabledContentColor = Color.Black,
                                   contentColor = Color.White
                                )
                                 ) {

                                Text(
                                    text = "Scan Document",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }




    /** Opens the Google document scanner UI. Only call this AFTER permission is confirmed granted. */
    /**
     * Starts the Google Document Scanner.
     * scanner  -> Google scanner object.
     * launcher -> Used to open another screen (scanner activity)
     *             and receive the result back.
     */

    private fun launchScanner(
        scanner: GmsDocumentScanner,
        launcher: ActivityResultLauncher<IntentSenderRequest>
    ) {

        // Ask Google Scanner to create an Intent that can start the scanner.
        scanner.getStartScanIntent(this@MainActivity)


            // Runs only if the Intent was created successfully.
            .addOnSuccessListener { intentSender ->

                // IntentSender = Special intent provided by Google.
                // IntentSenderRequest.Builder() converts it into a request that ActivityResultLauncher can launch.
                launcher.launch(IntentSenderRequest.Builder(intentSender).build())
            }

            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.message, Toast.LENGTH_LONG).show()
            }
    }



    /**
     * Saves images + PDF on a background thread, then hops back to the main
     * thread only to show the result Toast.
     *
     * try/catch is critical here: on devices below API 29, MediaStore.Downloads
     * doesn't exist and insert() throws — that was silently crashing the app
     * before. Now it's caught, and the user gets a real error message instead
     * of the app dying with no explanation.
     */

    /**
     * Saves all scanned pages and PDF.
     *
     * suspend -> Can run inside Coroutine.
     * pages   -> List of scanned image URIs.
     * pdfUri  -> PDF URI (can be null if no PDF exists).
     */

    private suspend fun saveScanResults(pages: List<Uri>, pdfUri: Uri?) {


        var savedImages = 0 // Counts how many images were saved successfully.
        var savedPdf = false // True if PDF saved successfully.
        var errorMessage: String? = null // Stores error message if any error happens.


        // Run file saving work on background thread.
        // File operations should NOT run on Main(UI) thread.
        withContext(Dispatchers.IO) {

            try {

                // Loop through every scanned page.
                // index = page number
                // pageUri = current image URI
                pages.forEachIndexed { index, pageUri ->

                    if (saveImageToGallery(pageUri, index)) savedImages++ // Save image into Gallery. If saved successfully, increase counter.
                }


                // ?.let means: Execute this block ONLY if pdfUri is NOT null.  ....Save PDF and store true/false.
                pdfUri?.let { savedPdf = savePdfToDocuments(it) }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error while saving"
            }
        }

        // Decide which message should be shown.
        val message = when {

            // If any error occurred.
            errorMessage != null -> "Save failed: $errorMessage"

            // If at least one image OR PDF was saved.
            savedImages > 0 || savedPdf ->
                "Saved $savedImages page(s) to Gallery" + if (savedPdf) " + PDF to Documents" else ""

            else -> "Nothing was saved — scan may have returned no pages"
        }


        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show() // Show final result to user
    }






    /** Saves one image to Pictures/DocScanner via MediaStore. Returns true on success. */

    /**
     * Saves ONE scanned image into Gallery.
     *
     * sourceUri -> Original image location.
     * index     -> Image number (0,1,2...)
     *
     * Returns:
     * true  -> Save successful
     * false -> Save failed
     */
    private fun saveImageToGallery(sourceUri: Uri, index: Int): Boolean {

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) // Create current date & time.
        val fileName = "SCAN_${timeStamp}_$index.jpg"  // Final image name.Example: SCAN_20260720_103500_0.jpg

        val contentValues = ContentValues().apply { // Information about new image.
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName) // File name.
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")// File type
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/DocScanner")  // Folder location inside Pictures.
        }

        val newImageUri = contentResolver.insert(   // Create empty image file inside MediaStore.
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: return false // Stop if file couldn't be created.

        contentResolver.openInputStream(sourceUri)?.use { input ->   // Open original image for reading.
            contentResolver.openOutputStream(newImageUri)?.use { output ->// Open new image for writing.
                input.copyTo(output)  // Copy image data.
            }

        } ?: return false

        return true  // Image saved successfully.
    }







    /**
     * Saves the PDF. Uses MediaStore.Downloads on API 29+ (required — public
     * Documents isn't writable via MediaStore before that). On API 28 and
     * below, falls back to a direct File() write, which needs
     * WRITE_EXTERNAL_STORAGE already granted (see manifest notes).
     */

    /**
    * Saves scanned PDF into Downloads/DocScanner.
    *
    * sourceUri -> Original PDF URI.
    *
    * Returns:
    * true  -> Saved successfully
    * false -> Failed
    */
    private fun savePdfToDocuments(sourceUri: Uri): Boolean {

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) // Current date & time.
        val fileName = "SCAN_$timeStamp.pdf" // PDF file name.


        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {  // Check Android version.
            val contentValues = ContentValues().apply {    // Android 10 (API 29) and above.
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)  // PDF file name.
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf") // File type.
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/DocScanner")   // Save location.
            }

            // Create new PDF entry.
            val newPdfUri = contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return false

            contentResolver.openInputStream(sourceUri)?.use { input ->  // Read original PDF.
                contentResolver.openOutputStream(newPdfUri)?.use { output ->  // Write into new PDF.
                    input.copyTo(output) // Copy all PDF bytes.
                }
            } ?: return false
            true
        } else {

            // Android 9 and below. Create Downloads/DocScanner folder.
            val dir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "DocScanner"
            )

            if (!dir.exists()) dir.mkdirs()  // Create folder if it doesn't exist.
            val outFile = File(dir, fileName) // Create final PDF file.

            contentResolver.openInputStream(sourceUri)?.use { input -> // Read original PDF.
                FileOutputStream(outFile).use { output -> // Write PDF into file.
                    input.copyTo(output) // Copy PDF data.
                }
            } ?: return false
            true


        }
    }
}







