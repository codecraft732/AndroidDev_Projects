package com.example.notesapp.viewmodel

import androidx.lifecycle.*
import com.example.notesapp.data.local.NotesData
import com.example.notesapp.data.repository.NoteRepository
import com.example.notesapp.util.ColorUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotesViewModel (private val repository: NoteRepository):  ViewModel(){
    val notes = repository.getNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


//Suspend function should be called only from a coroutine



    fun addNote(title:String, content: String) = viewModelScope.launch{
        val color = ColorUtil.getRandomColor()
        repository.addNote(NotesData(title= title, content = content , color = color))
    }


    fun updateNote(note: NotesData)= viewModelScope.launch {
        repository.updateNote(note)
    }

    fun deleteNote(note: NotesData)= viewModelScope.launch {
        repository.deleteNote(note)
    }


}
