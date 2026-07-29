package com.example.marigold.model.Note

import com.example.marigold.model.RemoteDao

class NoteRemoteDao : RemoteDao("tbl_notes") {
    fun fetchMemories() : List<Note> {
        val con = getConnection() ?: return emptyList()
        val listOfNote = arrayListOf<Note>()
        try {
            val resultSet = con.prepareStatement("SELECT * FROM $TABLE_NAME").executeQuery()
            while(resultSet.next()) {
                listOfNote.add(Note(
                    id = resultSet.getString("id"),
                    title = resultSet.getString("title"),
                    content = resultSet.getString("content"),
                    date = resultSet.getLong("date")
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            con.close()
        }
        return listOfNote
    }

}