package com.example.marigold.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.marigold.model.Media.Media
import com.example.marigold.model.Media.MediaDAO
import com.example.marigold.model.Note.Note
import com.example.marigold.model.Note.NoteDAO

@Database(
    entities = [Note::class, Media::class],
    version = 1,
    exportSchema = true
)
abstract class DB : RoomDatabase() {

    abstract fun noteDAO(): NoteDAO
    abstract  fun mediaDAO(): MediaDAO
}