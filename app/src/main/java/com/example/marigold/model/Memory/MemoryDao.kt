package com.example.marigold.model.Memory

import com.example.marigold.services.remoteSQLHandler
import java.sql.Connection

class MemoryDao {
    private fun getConnection(): Connection? = remoteSQLHandler().getSQLConnection()

    fun fetchMemories() : List<Memory> {
        val con = getConnection() ?: return emptyList()
        val listOfMemories = arrayListOf<Memory>()
        try {
            val resultSet = con.prepareStatement("SELECT * FROM tbl_memories").executeQuery()
            while(resultSet.next()) {
                listOfMemories.add(Memory(resultSet.getString("id"), resultSet.getString("memory")))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            con.close()
        }
        return listOfMemories
    }

    fun addMemory(memory: Memory) {
        val con = getConnection() ?: return
        try {
            con.prepareStatement("INSERT INTO tbl_memories (id, memory) VALUES (?, ?)").apply {
                setString(1, memory.id)
                setString(2, memory.memory)
                executeUpdate()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            con.close()
        }
    }
}