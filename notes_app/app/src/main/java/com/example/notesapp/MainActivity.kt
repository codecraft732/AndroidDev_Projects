package com.example.notesapp


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.*
import com.example.notesapp.data.local.*
import com.example.notesapp.data.repository.NoteRepository
import com.example.notesapp.ui.theme.NotesAppTheme
import com.example.notesapp.ui.theme.screens.*
import com.example.notesapp.util.ColorUtil
import com.example.notesapp.viewmodel.NotesViewModel



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                val db = NotesDatabase.getDatabase(this)
                val repository = NoteRepository(db.NoteDao())
                val factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return NotesViewModel(repository) as T
                    }
                }


                val viewModel: NotesViewModel = viewModel(factory = factory)
                var selectedNote by remember { mutableStateOf<NotesData?>(null) }
                var editingNote by remember { mutableStateOf<NotesData?>(null) }

                val context = LocalContext.current
                if (selectedNote == null) {
                    MainScreen(
                        viewModel = viewModel,
                        onCreateNote = {
                            // navigate to blank NoteScreen
                            editingNote = null
                            selectedNote = NotesData(id = 0, title = "", content = "", color = ColorUtil.getRandomColor() )

                        },
                        onOpenNote = { note ->
                            selectedNote = note
                            editingNote = note

                        },

                    )
                } else {
                    NoteScreen(
                        viewModel = viewModel,
                        editingNote = editingNote,
                        onDoneEditing = {
                            editingNote= null
                        },
                        onBack = { selectedNote = null },

                            onDelete = { note ->
                            viewModel.deleteNote(note)
                            selectedNote = null
                            Toast.makeText(
                                context,
                                "Note deleted successfully",
                                Toast.LENGTH_SHORT
                                ).show()
                        }
                    )
                }
            }
        }
    }
}

