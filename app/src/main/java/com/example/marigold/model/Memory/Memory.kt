package com.example.marigold.model.Memory

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tbl_memories")
data class Memory (
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var memory: String = ""
)