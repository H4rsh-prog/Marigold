package com.example.marigold.model.Memory

import com.example.marigold.model.RemoteDao

class MemoryRemoteDao : RemoteDao<Memory, String>("tbl_memories", Memory::class.java, "id")