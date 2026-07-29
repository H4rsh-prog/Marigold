package com.example.marigold.model.Note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.marigold.model.Memory.Memory

@Dao
interface MemoryRoomDao {
    @Upsert(entity = Memory::class)
    suspend fun upsert(memory: Memory)
    @Delete(entity = Memory::class)
    suspend fun delete(memory: Memory)
    @Query("SELECT * FROM tbl_memories WHERE id = :id")
    suspend fun getById(id: String): Memory?
    @Query("DELETE FROM tbl_memories WHERE id = :id")
    suspend fun deleteById(id: String)
    @Query("SELECT * FROM tbl_memories")
    suspend fun getAll(): List<Memory>
    @Query("DELETE FROM tbl_memories")
    suspend fun deleteAll()
}