package com.example.marigold.model.Media

import android.content.Context
import android.net.Uri
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.InputFile
import io.appwrite.services.Storage
import java.io.File

object Appwrite {
    val bucketID = "6a76b7f9001695648128"
    lateinit var client : Client
    lateinit var storage : Storage

    fun init(context: Context) {
        client = Client(context = context)
            .setEndpoint("https://sgp.cloud.appwrite.io/v1")
            .setProject("marigold-cdn")
        storage = Storage(client = client)
    }

    suspend fun getFile(id : String) : io.appwrite.models.File {
        return storage.getFile(
            bucketId = bucketID,
            fileId = id
        )
    }
    fun getFileCDN(id : String) : String {
        return "https://sgp.cloud.appwrite.io/v1/storage/buckets/$bucketID/files/$id/view?project=marigold-cdn&impersonateuserid=&mode=admin"
    }

    suspend fun storeFile(context: Context, uri : Uri) : io.appwrite.models.File {
        val file = File(context.cacheDir, "temp_file_${System.currentTimeMillis()}")
        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val response = storage.createFile(
            bucketId = bucketID,
            fileId = ID.unique(),
            file = InputFile.fromFile(file),
        )
        file.delete()
        return response
    }
}