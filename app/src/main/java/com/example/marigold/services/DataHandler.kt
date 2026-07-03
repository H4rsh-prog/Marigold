package com.example.marigold.services

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class DataHandler(context: Context) {
    val DEFINE_MARGIOLD = "DEFINE_MARGIOLD";
    val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    );
    fun saveData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply();
    };
    fun getData(key: String): String? {
        return sharedPreferences.getString(key, null);
    };
    fun removeData(key: String) {
        sharedPreferences.edit().remove(key).apply();
    }
    fun isAppInitialized() : Boolean {
        return sharedPreferences.contains(DEFINE_MARGIOLD);
    };
}