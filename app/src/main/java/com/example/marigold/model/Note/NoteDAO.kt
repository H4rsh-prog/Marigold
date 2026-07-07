package com.example.marigold.model.Note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NoteDAO {
    @Upsert(entity = Note::class)
    suspend fun upsertNote(note: Note)
    @Delete(entity = Note::class)
    suspend fun deleteNote(note: Note)
    @Query("SELECT * FROM tbl_notes WHERE id = :id")
    suspend fun getNoteById(id: String): Note?
    @Query("DELETE FROM tbl_notes WHERE id = :id")
    suspend fun deleteNoteById(id: String)
    @Query("SELECT * FROM tbl_notes")
    suspend fun getAllNotes(): List<Note>
    @Query("DELETE FROM tbl_notes")
    suspend fun deleteAllNotes()
}