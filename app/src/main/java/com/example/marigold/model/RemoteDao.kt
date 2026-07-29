package com.example.marigold.model

import com.example.marigold.services.remoteSQLHandler
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.sql.Connection

open class RemoteDao<clazz> {
    var TABLE_NAME : String
    var ENTITY_TYPE : Class<clazz>
    constructor(table_name: String, entity_type : Class<clazz>) {
        this.TABLE_NAME = table_name
        this.ENTITY_TYPE = entity_type
    }
    protected fun getConnection(): Connection? = remoteSQLHandler().getSQLConnection()
    //HELPER FUNCTION TO RFILTER SYNTHETIC AND STATIC FIELDS OF KOTLIN
    private fun getPersistentFields(cls: Class<*>): List<Field> {
        return cls.declaredFields.filter { field ->
            !field.isSynthetic &&
                    !Modifier.isStatic(field.modifiers) &&
                    !Modifier.isTransient(field.modifiers)
        }.also { fields ->
            // Make accessible
            fields.forEach { it.isAccessible = true }
        }
    }
    fun fetch() : List<clazz> {
        val con = this.getConnection() ?: return emptyList()
        val listOfEntities = arrayListOf<clazz>()
        try {
            val resultSet = con.prepareStatement("SELECT * FROM $TABLE_NAME").executeQuery()
            val fields = getPersistentFields(ENTITY_TYPE)
            while(resultSet.next()) {
                var entity = ENTITY_TYPE.constructors.first().newInstance()
                fields.forEach { field ->
                    when(field.type) {
                        Int::class.java -> field.setInt(entity, resultSet.getInt(field.name))
                        Long::class.java -> field.setLong(entity, resultSet.getLong(field.name))
                        Boolean::class.java -> field.setBoolean(entity, resultSet.getBoolean(field.name))
                        Float::class.java -> field.setFloat(entity, resultSet.getFloat(field.name))
                        Double::class.java -> field.setDouble(entity, resultSet.getDouble(field.name))
                        else -> field.set(entity, resultSet.getString(field.name))
                    }
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
            val fields = getPersistentFields(ENTITY_TYPE)
            var query = "INSERT INTO $TABLE_NAME ("
            fields.forEach { field ->
                query += "${field.name},"
            }
            query = query.dropLast(1)
            query += ") VALUES ("
            fields.forEach { field ->
                query += "?,"
            }
            query = query.dropLast(1)
            query += ")"

            con.prepareStatement(query).apply {
                fields.forEachIndexed { index, field ->
                    val value = field.get(entity)
                    setObject(index+1, value)
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