package com.example.marigold.model.Memory

import com.example.marigold.model.RemoteDao

class MemoryRemoteDao : RemoteDao<Memory>("tbl_memories", Memory::class.java)