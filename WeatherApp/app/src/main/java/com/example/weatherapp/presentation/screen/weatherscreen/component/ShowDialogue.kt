package com.example.weatherapp.presentation.screen.weatherscreen.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShowDialogue(
    modifier: Modifier = Modifier,
    currentQuery: String,
    onDismiss: () -> Unit,
    onSearch: (String) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Responsive font sizes
    val titleSize = (screenHeight.value * 0.025).sp
    val labelSize = (screenHeight.value * 0.018).sp

    var searchText by remember { mutableStateOf(currentQuery) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (searchText.isNotBlank()) {
                        onSearch(searchText)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6),
                    contentColor = Color(0xFF3B82F6),
                )
            ) {
                Text(text = "Search", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss) {
                Text("Cancel",
                    color = Color(0xFF3B82F6)
                )
            }
        },
        title = {
            Text(
                text = "Search City",
                fontSize = titleSize,
                style = MaterialTheme.typography.headlineSmall,
                color =Color(0xFF3B82F6)
            )
        },
        text = {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text(
                    text = "City name",
                    fontSize = labelSize,
                    color = Color(0xFF3B82F6),
                ) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFF3B82F6),
                    focusedBorderColor = Color(0xFF3B82F6),
                    focusedLabelColor = Color(0xFF3B82F6),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedPlaceholderColor = Color(0xFF3B82F6),
                )
            )
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp)
    )
}
