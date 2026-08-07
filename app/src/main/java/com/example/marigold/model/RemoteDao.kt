package com.example.marigold.model

import com.example.marigold.services.RemoteSQLHandler
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.sql.Connection

open class RemoteDao<clazz, pk_type> (
    var TABLE_NAME : String,
    var ENTITY_TYPE : Class<clazz>,
    var PK_NAME : String = "id"
) {
    protected fun getConnection(): Connection? = RemoteSQLHandler().getSQLConnection()
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
            fields.forEach { _ ->
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
    fun remove(primaryKey: pk_type) {
        val con = getConnection() ?: return
        try {
            con.prepareStatement("DELETE FROM $TABLE_NAME WHERE ${PK_NAME} = ?").apply {
                setObject(1, primaryKey)
                executeUpdate()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        } finally {
            con.close()
        }
    }
    fun update(entity: clazz) {
        entity ?: return
        val con = getConnection() ?: return
        try {
            val fields = getPersistentFields(ENTITY_TYPE)
            val primaryKeyField = fields.find { it.name==PK_NAME } ?: throw Exception("No primary key found")
            val setClause = fields.filter { it.name != PK_NAME }.joinToString(", ") { "${it.name} = ?" }
            var query = "UPDATE $TABLE_NAME SET $setClause WHERE $PK_NAME = ?"
            con.prepareStatement(query).apply {
                var index = 1
                fields.forEach { field ->
                    if(field.name!=PK_NAME) {
                        setObject(index++, field.get(entity))
                    }
                }
                setObject(index, primaryKeyField.get(entity))
                executeUpdate()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        } finally {
            con.close()
        }
    }
}