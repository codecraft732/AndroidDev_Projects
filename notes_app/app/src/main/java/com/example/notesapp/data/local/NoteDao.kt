package com.example.notesapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao //data access object crud operations
interface NoteDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE) //if id already exist change or replace it with new
    suspend fun Insert(note: NotesData) //TO ADD  new note

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NotesData>> //to find all notes

    @Update
    suspend fun update(note: NotesData)  // To update note

    @Delete
    suspend fun delete(note: NotesData)   // To delete note

}