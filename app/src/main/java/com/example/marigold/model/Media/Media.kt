package com.example.marigold.model.Media

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tbl_media")
data class Media (
    @PrimaryKey
    var id : String = UUID.randomUUID().toString(),
    var title : String = "",
    var date : Long = System.currentTimeMillis(),
    var uri : String = "",
    @ColumnInfo(defaultValue = "0")
    var width : Int = 0,
    @ColumnInfo(defaultValue = "0")
    var height : Int = 0,
)