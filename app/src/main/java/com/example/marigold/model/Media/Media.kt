package com.example.marigold.model.Media

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tbl_media")
data class Media (
    @PrimaryKey
    var id : String = UUID.randomUUID().toString(),
    var date : Long = System.currentTimeMillis(),
    var uri : String = "",
)