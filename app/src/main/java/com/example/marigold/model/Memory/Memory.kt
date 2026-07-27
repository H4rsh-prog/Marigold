package com.example.marigold.model.Memory

import java.util.UUID

data class Memory (
    var id: String = UUID.randomUUID().toString(),
    var memory: String
)