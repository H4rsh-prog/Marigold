package com.example.marigold.model

import com.example.marigold.services.remoteSQLHandler
import java.sql.Connection

open class RemoteDao {
    var TABLE_NAME : String
    constructor(TABLE_NAME: String) {
        this.TABLE_NAME = TABLE_NAME
    }
    protected fun getConnection(): Connection? = remoteSQLHandler().getSQLConnection()
    fun <clazz> add(entity: clazz) {
        if(entity == null) return
        val con = getConnection() ?: return
        try {
            var query = "INSERT INTO $TABLE_NAME ("
            entity.javaClass.fields.forEach { field ->
                query += "${field.name},"
            }
            query = query.dropLast(1)
            query += ") VALUES ("
            entity.javaClass.fields.forEach { field ->
                query += "?,"
            }
            query = query.dropLast(1)
            query += ")"

            con.prepareStatement(query).apply {
                entity.javaClass.fields.forEachIndexed { index, field ->
                    setString(index+1, field?.get(entity).toString())
                }
                executeUpdate()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            con.close()
        }
    }
}