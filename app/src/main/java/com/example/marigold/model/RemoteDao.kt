package com.example.marigold.model

import com.example.marigold.services.remoteSQLHandler
import java.sql.Connection

open class RemoteDao<clazz> {
    var TABLE_NAME : String
    lateinit var ENTITY_TYPE : Class<clazz>
    constructor(TABLE_NAME: String) {
        this.TABLE_NAME = TABLE_NAME
    }
    protected fun getConnection(): Connection? = remoteSQLHandler().getSQLConnection()

    fun fetch() : List<clazz> {
        val con = this.getConnection() ?: return emptyList()
        val listOfEntities = arrayListOf<clazz>()
        try {
            val resultSet = con.prepareStatement("SELECT * FROM $TABLE_NAME").executeQuery()
            while(resultSet.next()) {
                var entity = ENTITY_TYPE.constructors.first().newInstance()
                ENTITY_TYPE.javaClass.fields.forEach { field ->
                    field.set(entity,
                        when(field.type) {
                            String -> resultSet.getString(field.name)
                            Int -> resultSet.getInt(field.name)
                            Long -> resultSet.getLong(field.name)
                            Float -> resultSet.getFloat(field.name)
                            Double -> resultSet.getDouble(field.name)
                            Boolean -> resultSet.getBoolean(field.name)
                            else -> resultSet.getObject(field.name)
                        }
                    )
                }
                listOfEntities.add(entity as clazz)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            con.close()
        }
        return listOfEntities
    }
    fun add(entity: clazz) {
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