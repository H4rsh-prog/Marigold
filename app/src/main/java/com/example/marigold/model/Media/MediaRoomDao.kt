package com.example.marigold.model.Media

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MediaRoomDao {
    @Upsert(entity = Media::class)
    suspend fun upsert(media: Media)
    @Delete(entity = Media::class)
    suspend fun delete(media: Media)
    @Query("SELECT * FROM tbl_media WHERE id = :id")
    suspend fun getById(id: String): Media?
    @Query("DELETE FROM tbl_media WHERE id = :id")
    suspend fun deleteById(id: String)
    @Query("SELECT * FROM tbl_media")
    suspend fun getAll(): List<Media>
    @Query("DELETE FROM tbl_media")
    suspend fun deleteAll()
}