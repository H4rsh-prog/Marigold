package com.example.marigold.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.marigold.model.Media.Media
import com.example.marigold.model.Media.MediaRoomDao
import com.example.marigold.model.Memory.Memory
import com.example.marigold.model.Note.Note
import com.example.marigold.model.Note.NoteRoomDao

@Database(
    entities = [Note::class, Media::class, Memory::class],
    version = 2,
    exportSchema = true
)
abstract class DB : RoomDatabase() {

    abstract fun noteDAO(): NoteRoomDao
    abstract  fun mediaDAO(): MediaRoomDao
}