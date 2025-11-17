package com.tunnexa.data.storage

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings

/**
 * Secure preferences manager for storing device ID and authentication token
 */
class SecurePreferencesManager private constructor(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val PREFS_NAME = "tunnexa_secure_prefs"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_TOKEN = "auth_token"
        
        @Volatile
        private var INSTANCE: SecurePreferencesManager? = null
        
        fun getInstance(context: Context): SecurePreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SecurePreferencesManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
    
    /**
     * Get or generate device ID
     * Uses Android's Settings.Secure.ANDROID_ID as the device identifier
     */
    fun getDeviceId(): String {
        val storedDeviceId = prefs.getString(KEY_DEVICE_ID, null)
        return storedDeviceId ?: run {
            // Generate device ID from Android ID
            val androidId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            ) ?: generateFallbackDeviceId()
            
            // Store it for future use
            prefs.edit().putString(KEY_DEVICE_ID, androidId).apply()
            androidId
        }
    }
    
    /**
     * Store authentication token securely
     */
    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }
    
    /**
     * Get stored authentication token
     */
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }
    
    /**
     * Check if token exists
     */
    fun hasToken(): Boolean {
        return getToken() != null
    }
    
    /**
     * Clear stored token (for logout)
     */
    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }
    
    /**
     * Clear all stored data
     */
    fun clearAll() {
        prefs.edit().clear().apply()
    }
    
    /**
     * Generate fallback device ID if Android ID is not available
     */
    private fun generateFallbackDeviceId(): String {
        return "device_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }
}

