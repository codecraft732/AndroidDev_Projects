package com.example.notesapp.ui.theme.screens


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.SegmentedButtonDefaults.borderStroke
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.notesapp.data.local.NotesData
import com.example.notesapp.viewmodel.NotesViewModel
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun ColumnCards(
    note: NotesData,
    color: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {


    val hasTitle = note.title.isNotBlank()
    val hasContent = note.content.isNotBlank()

    if (!hasTitle && !hasContent) return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()

                .padding(2.dp)
                .clickable {
                    onClick()
                }

                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(
                        38.dp
                    )
                ),
            colors = CardDefaults.cardColors(containerColor = color)

        ) {
            content()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: NotesViewModel,
    onCreateNote: () -> Unit,
    onOpenNote: (NotesData) -> Unit,
) {

    var search by rememberSaveable { mutableStateOf("") }
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val notesState by viewModel.notes.collectAsState()


//     notesState is now a plain List<NotesData>, so .filter works correctly
    val filteredNotes = remember(notesState, search) {
        if (search.isBlank()) notesState
        else notesState.filter { note ->
            note.title.contains(search, ignoreCase = true) ||
                    note.content.contains(search, ignoreCase = true)
        }
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onCreateNote()

                },
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(62.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top

        ) {

            TextField(
                value = search,
                onValueChange = { search = it },

                placeholder = { Text("Search Notes") },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                leadingIcon = {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = " ",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )

                },

                shape = RoundedCornerShape(24.dp),
                singleLine = true,

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White,
                    unfocusedLeadingIconColor = Color.White,
                    focusedLeadingIconColor = Color.White
                )
            )
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(filteredNotes, key = { note -> note.id }) { note ->

                    val hasTitle = note.title.isNotBlank()
                    val hasContent = note.content.isNotBlank()
                    ColumnCards(
                        note = note,
                        onClick = {
                            onOpenNote(
                                note
                            )
                        },
                        color = Color(note.color)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {

                            Text(
                                "Created: ${sdf.format(Date(note.timestamp))}",
                                style = MaterialTheme.typography.labelSmall
                            )

                            if (hasTitle) {
                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontFamily = FontFamily.SansSerif,
                                    maxLines = 3,
                                    modifier = Modifier.padding(12.dp),
                                    overflow = TextOverflow.Ellipsis

                                )
                            }
                            if (hasTitle && hasContent) {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.padding(horizontal = 14.dp)
                                )
                            }

                            if (hasContent) {
                                Text(
                                    text = note.content.ifBlank { "Empty note" },
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = FontFamily.SansSerif,
                                    maxLines = 10,
                                    modifier = Modifier.padding(18.dp),
                                    overflow = TextOverflow.Ellipsis

                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

