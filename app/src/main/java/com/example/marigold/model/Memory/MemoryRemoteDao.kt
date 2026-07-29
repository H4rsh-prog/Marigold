package com.example.marigold.model.Memory

import com.example.marigold.model.RemoteDao

class MemoryRemoteDao : RemoteDao("tbl_memories") {
    fun fetchMemories() : List<Memory> {
        val con = getConnection() ?: return emptyList()
        val listOfMemories = arrayListOf<Memory>()
        try {
            val resultSet = con.prepareStatement("SELECT * FROM $TABLE_NAME").executeQuery()
            while(resultSet.next()) {
                listOfMemories.add(Memory(
                    id = resultSet.getString("id"),
                    memory = resultSet.getString("memory")
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            con.close()
        }
        return listOfMemories
    }
}