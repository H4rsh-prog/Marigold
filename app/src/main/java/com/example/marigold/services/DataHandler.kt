package com.example.marigold.services

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class DataHandler(private val context: Context) {
    val DEFINE_MARIGOLD = "DEFINE_MARIGOLD"
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    internal val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    fun savePreference(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    fun getPreference(key: String): String? {
        return try {
            sharedPreferences.getString(key, null)
        } catch (e: Exception) {
            sharedPreferences.edit().clear().apply()
            null
        }
    }
    fun removePreference(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
    fun isAppInitialized(): Boolean {
        return try {
            sharedPreferences.contains(DEFINE_MARIGOLD)
        } catch (e: Exception) {
            sharedPreferences.edit().clear().apply()
            false
        }
    }
    suspend fun uploadData(){
        //TO IMPLEMENT
    }
}