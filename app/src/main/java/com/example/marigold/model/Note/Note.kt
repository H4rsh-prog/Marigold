package com.example.marigold.model.Note

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tbl_notes")
data class Note(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var content: String = "",
    var date: Long = System.currentTimeMillis()
)