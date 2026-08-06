package com.example.marigold.model.Note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NoteRoomDao {
    @Upsert(entity = Note::class)
    suspend fun upsert(note: Note)
    @Upsert(entity = Note::class)
    suspend fun upsertAll(note: List<Note>)
    @Delete(entity = Note::class)
    suspend fun delete(note: Note)
    @Query("SELECT * FROM tbl_notes WHERE id = :id")
    suspend fun getById(id: String): Note?
    @Query("DELETE FROM tbl_notes WHERE id = :id")
    suspend fun deleteById(id: String)
    @Query("SELECT * FROM tbl_notes")
    suspend fun getAll(): List<Note>
    @Query("DELETE FROM tbl_notes")
    suspend fun deleteAll()
}