package com.example.notesapp.ui.theme.screens


import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import com.example.notesapp.data.local.NotesData
import com.example.notesapp.viewmodel.NotesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    viewModel: NotesViewModel,
    editingNote: NotesData? = null,
    onDoneEditing: () -> Unit,
    onBack: () -> Unit,
    onDelete: (NotesData) -> Unit
) {
    var isEditing by rememberSaveable { mutableStateOf(true) }
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var currentEditing by remember { mutableStateOf<NotesData?>(null) }

    LaunchedEffect(editingNote) {
        if (editingNote != null) {
            currentEditing = editingNote
            title = editingNote.title
            content = editingNote.content
            isEditing = false
            onDoneEditing()
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = maxWidth
        
        // Responsive variables based on screen width
        val horizontalPadding = if (screenWidth > 600.dp) 48.dp else 16.dp
        val titleFontSize = if (screenWidth > 600.dp) 24.sp else 18.sp
        val contentFontSize = if (screenWidth > 600.dp) 18.sp else 16.sp
        val buttonHeight = if (screenWidth > 600.dp) 52.dp else 44.dp
        val maxColumnWidth = if (screenWidth > 1200.dp) 900.dp else 750.dp
        val topBarIconSize = if (screenWidth > 600.dp) 40.dp else 32.dp

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    title = {},
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier
                                .padding(horizontal = horizontalPadding)
                                .size(topBarIconSize)
                                .background(
                                    color = Color.LightGray.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(if (screenWidth > 600.dp) 10.dp else 8.dp)
                                .clickable { onBack() }
                        )
                    },
                    actions = {
                        var expanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                modifier = Modifier
                                    .size(if (screenWidth > 600.dp) 32.dp else 24.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { expanded = true }
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Delete") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        currentEditing?.let { onDelete(it) }
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text("Edit") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.EditNote,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        isEditing = true
                                    }
                                )
                            }
                        }
                    }
                )
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
                        .fillMaxHeight()
                        .widthIn(max = maxColumnWidth)
                        .padding(horizontal = horizontalPadding)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Title", fontSize = titleFontSize) },
                        textStyle = TextStyle(
                            fontSize = titleFontSize,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = if (screenWidth > 600.dp) 24.dp else 16.dp),
                        readOnly = !isEditing,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.LightGray.copy(alpha = 0.5f)
                    )

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        placeholder = { Text("Note...", fontSize = contentFontSize) },
                        textStyle = TextStyle(
                            fontSize = contentFontSize,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.Serif,
                            lineHeight = if (screenWidth > 600.dp) 28.sp else 24.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        readOnly = !isEditing,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray,
                            cursorColor = Color.Black,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Button(
                        onClick = {
                            if (title.isNotEmpty() || content.isNotEmpty()) {
                                if (currentEditing == null) {
                                    viewModel.addNote(title, content)
                                } else {
                                    viewModel.updateNote(
                                        currentEditing!!.copy(
                                            title = title, content = content
                                        )
                                    )
                                    currentEditing = null
                                    isEditing = false
                                }
                                title = ""
                                content = ""
                                onBack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = if (screenWidth > 600.dp) 32.dp else 16.dp)
                            .height(buttonHeight),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                        ),
                    ) {
                        Text(
                            text = if (currentEditing == null) "Add Note" else "Update Note",
                            color = Color.White,
                            fontSize = if (screenWidth > 600.dp) 16.sp else 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
