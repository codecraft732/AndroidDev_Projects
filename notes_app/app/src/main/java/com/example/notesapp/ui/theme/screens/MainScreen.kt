package com.example.notesapp.ui.theme.screens


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextOverflow
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
    screenWidth: Dp,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val hasTitle = note.title.isNotBlank()
    val hasContent = note.content.isNotBlank()

    if (!hasTitle && !hasContent) return

    val cardPadding = if (screenWidth > 600.dp) 2.dp else 1.dp
    val cardCornerRadius = if (screenWidth > 600.dp) 16.dp else 12.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(cardPadding)
            .heightIn(min = if (screenWidth > 600.dp) 180.dp else 130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(cardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        content()
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

    val filteredNotes = remember(notesState, search) {
        if (search.isBlank()) notesState
        else notesState.filter { note ->
            note.title.contains(search, ignoreCase = true) ||
                    note.content.contains(search, ignoreCase = true)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = maxWidth

        val horizontalPadding = if (screenWidth > 600.dp) 28.dp else 12.dp
        val gridCellsMinSize = if (screenWidth > 600.dp) 200.dp else 160.dp
        val contentMaxWidth = if (screenWidth > 1200.dp) 1100.dp else 900.dp
        val fabSize = if (screenWidth > 600.dp) 70.dp else 62.dp
        val fabIconSize = if (screenWidth > 600.dp) 30.dp else 25.dp
        val searchBarVerticalPadding = if (screenWidth > 600.dp) 16.dp else 12.dp

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onCreateNote,
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.size(fabSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Note",
                        tint = Color.White,
                        modifier = Modifier.size(fabIconSize)
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = contentMaxWidth),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    val searchBarHeight = if (screenWidth > 600.dp) 48.dp else 44.dp
                    val searchFontSize = if (screenWidth > 600.dp) 15.sp else 14.sp

                    BasicTextField(
                        value = search,
                        onValueChange = { search = it },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = searchFontSize,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = horizontalPadding,
                                vertical = searchBarVerticalPadding
                            )
                            .height(searchBarHeight),
                        singleLine = true,
                        cursorBrush = SolidColor(Color.White),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .background(Color.Black, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.White,
                                    modifier = Modifier.size(if (screenWidth > 600.dp) 20.dp else 18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(modifier = Modifier.weight(1f)) {
                                    if (search.isEmpty()) {
                                        Text(
                                            text = "Search Notes",
                                            color = Color.White.copy(alpha = 0.7f),
                                            fontSize = searchFontSize
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        }
                    )

                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(minSize = gridCellsMinSize),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = horizontalPadding),
                        verticalItemSpacing = if (screenWidth > 600.dp) 14.dp else 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(if (screenWidth > 600.dp) 16.dp else 8.dp),
                        contentPadding = PaddingValues(bottom = fabSize + 32.dp)
                    ) {
                        items(filteredNotes, key = { note -> note.id }) { note ->
                            ColumnCards(
                                note = note,
                                color = Color(note.color),
                                screenWidth = screenWidth,
                                onClick = { onOpenNote(note) }
                            ) {
                                NoteItemContent(note, sdf, screenWidth)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteItemContent(note: NotesData, sdf: SimpleDateFormat, screenWidth: Dp) {
    val hasTitle = note.title.isNotBlank()
    val hasContent = note.content.isNotBlank()

    val itemPadding = if (screenWidth > 600.dp) 32.dp else 24.dp
    val titleFontSize = if (screenWidth > 600.dp) 18.sp else 16.sp
    val contentFontSize = if (screenWidth > 600.dp) 14.sp else 13.sp
    val contentMaxLines = if (screenWidth > 1200.dp) 20 else if (screenWidth > 600.dp) 18 else 12

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(itemPadding)
    ) {
        Text(
            text = "Created: ${sdf.format(Date(note.timestamp))}",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = if (screenWidth > 600.dp) 13.sp else 11.sp
            ),
            color = Color.White.copy(alpha = 0.8f)
        )

        if (hasTitle) {
            Text(
                text = note.title,
                style = TextStyle(
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                ),
                maxLines = 2,
                modifier = Modifier.padding(top = 8.dp),
                overflow = TextOverflow.Ellipsis
            )
        }

        if (hasTitle && hasContent) {
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (hasContent) {
            Text(
                text = note.content,
                style = TextStyle(
                    fontSize = contentFontSize,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = contentMaxLines,
                modifier = Modifier.padding(top = if (hasTitle) 5.dp else 8.dp),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
