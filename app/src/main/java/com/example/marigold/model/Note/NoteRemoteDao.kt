package com.example.marigold.model.Note

import com.example.marigold.model.RemoteDao

class NoteRemoteDao : RemoteDao<Note>("tbl_notes", Note::class.java)