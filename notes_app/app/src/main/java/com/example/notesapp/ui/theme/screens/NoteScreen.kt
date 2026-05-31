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
import androidx.compose.runtime.setValue
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
            isEditing = false   // only read mode by default
            onDoneEditing()

        }
    }


    Column(
        modifier = Modifier.fillMaxSize()
    )
    {
        Scaffold(

            containerColor = Color.Transparent,
            topBar = {

                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.statusBarsPadding(),
                    title = {},
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            tint = Color.Black,
                            modifier = Modifier
                                .padding(horizontal = 18.dp)
                                .size(28.dp)
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 4.dp)
                                .clickable {
                                    onBack()
                                }
                        )
                    },

                    actions = {
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        expanded = true
                                    }
                                    .size(24.dp)
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
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {


                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },

                    placeholder = { Text("Title") },

                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        ),

                    modifier = Modifier
                        .padding(18.dp)
                        .fillMaxWidth(),
                    readOnly = !isEditing,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedPlaceholderColor = Color.DarkGray,
                        unfocusedPlaceholderColor = Color.DarkGray,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black
                    )
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(horizontal = 28.dp)
                )

                OutlinedTextField(

                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Note...") },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.Serif
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(18.dp)
                        .verticalScroll(rememberScrollState()),
                    readOnly = !isEditing,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedPlaceholderColor = Color.DarkGray,
                        unfocusedPlaceholderColor = Color.DarkGray,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black
                    )


                )



                Button(
                    onClick = {
                        if (title.isNotEmpty() && content.isNotEmpty()) {

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
                            //this means to clear feild after add or update
                            title = ""
                            content = ""
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            color = Color.Black, shape = RoundedCornerShape(1.dp)
                        ),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,

                    ),

                    ) {
                    Text(if (currentEditing == null) "Add Note" else "Update Note",
                        color = Color.White)
                }
            }
        }
    }
}

