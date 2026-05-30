package com.example.notesapp.data.repository

import com.example.notesapp.data.local.*
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val dao : NoteDao) {
    fun getNotes(): Flow<List<NotesData>> = dao.getAllNotes()
    suspend fun addNote(note: NotesData) = dao.Insert(note)
    suspend fun updateNote(note: NotesData) = dao.update(note)
    suspend fun deleteNote(note: NotesData)  = dao.delete(note)

}