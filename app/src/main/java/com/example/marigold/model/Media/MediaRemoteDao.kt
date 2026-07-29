package com.example.marigold.model.Media

import com.example.marigold.model.RemoteDao

class MediaRemoteDao : RemoteDao("tbl_media") {
    fun fetchMemories() : List<Media> {
        val con = getConnection() ?: return emptyList()
        val listOfMedia = arrayListOf<Media>()
        try {
            val resultSet = con.prepareStatement("SELECT * FROM $TABLE_NAME").executeQuery()
            while(resultSet.next()) {
                listOfMedia.add(Media(
                    id = resultSet.getString("id"),
                    date = resultSet.getLong("date"),
                    uri = resultSet.getString("uri")
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            con.close()
        }
        return listOfMedia
    }
}