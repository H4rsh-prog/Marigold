package com.example.marigold.model.Media

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MediaDAO {
    @Upsert(entity = Media::class)
    suspend fun upsertMedia(media: Media)
    @Delete(entity = Media::class)
    suspend fun deleteMedia(media: Media)
    @Query("SELECT * FROM tbl_media WHERE id = :id")
    suspend fun getMediaById(id: String): Media?
    @Query("DELETE FROM tbl_media WHERE id = :id")
    suspend fun deleteMediaById(id: String)
    @Query("SELECT * FROM tbl_media")
    suspend fun getAllMedia(): List<Media>
    @Query("DELETE FROM tbl_media")
    suspend fun deleteAllMedia()
}