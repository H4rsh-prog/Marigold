package com.example.marigold.services

import com.example.marigold.BuildConfig
import java.sql.Connection
import java.sql.DriverManager

class remoteSQLHandler {
    fun getSQLConnection(): Connection? {
        return try {
            Class.forName("com.mysql.jdbc.Driver")
            DriverManager.getConnection(
                "jdbc:mysql://${BuildConfig.dataSourceHost}:${BuildConfig.dataSourcePort}/${BuildConfig.dataSourceDB}",
                BuildConfig.dataSourceUsername,
                BuildConfig.dataSourcePassword
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}