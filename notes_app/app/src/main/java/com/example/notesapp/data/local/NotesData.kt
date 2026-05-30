package com.example.notesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class NotesData (

    @PrimaryKey(autoGenerate = true) // primary key use for unique id generate automatically
    val id:Int = 0,

    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val color: Long

)