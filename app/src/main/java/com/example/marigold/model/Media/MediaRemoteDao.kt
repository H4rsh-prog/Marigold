package com.example.marigold.model.Media

import com.example.marigold.model.RemoteDao

class MediaRemoteDao : RemoteDao<Media, String>("tbl_media", Media::class.java, "id")