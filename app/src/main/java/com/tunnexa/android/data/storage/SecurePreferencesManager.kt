package com.tunnexa.android.data.storage

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
        private const val KEY_FAVORITE_SERVERS = "favorite_servers"
        private const val KEY_SELECTED_SERVER = "selected_server"
        private const val KEY_SPLIT_TUNNEL_APPS = "split_tunnel_apps"
        private const val KEY_SPLIT_TUNNEL_ENABLED = "split_tunnel_enabled"
        
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
    
    /**
     * Get favorite server IDs
     */
    fun getFavoriteServers(): Set<String> {
        return prefs.getStringSet(KEY_FAVORITE_SERVERS, emptySet()) ?: emptySet()
    }
    
    /**
     * Add server to favorites
     */
    fun addFavoriteServer(serverId: String) {
        val favorites = getFavoriteServers().toMutableSet()
        favorites.add(serverId)
        prefs.edit().putStringSet(KEY_FAVORITE_SERVERS, favorites).apply()
    }
    
    /**
     * Remove server from favorites
     */
    fun removeFavoriteServer(serverId: String) {
        val favorites = getFavoriteServers().toMutableSet()
        favorites.remove(serverId)
        prefs.edit().putStringSet(KEY_FAVORITE_SERVERS, favorites).apply()
    }
    
    /**
     * Toggle favorite status of a server
     */
    fun toggleFavoriteServer(serverId: String) {
        val favorites = getFavoriteServers().toMutableSet()
        if (favorites.contains(serverId)) {
            favorites.remove(serverId)
        } else {
            favorites.add(serverId)
        }
        prefs.edit().putStringSet(KEY_FAVORITE_SERVERS, favorites).apply()
    }
    
    /**
     * Check if server is favorite
     */
    fun isFavoriteServer(serverId: String): Boolean {
        return getFavoriteServers().contains(serverId)
    }
    
    /**
     * Get selected server ID
     */
    fun getSelectedServer(): String? {
        return prefs.getString(KEY_SELECTED_SERVER, null)
    }
    
    /**
     * Set selected server ID
     */
    fun setSelectedServer(serverId: String) {
        prefs.edit().putString(KEY_SELECTED_SERVER, serverId).apply()
    }
    
    /**
     * Clear selected server
     */
    fun clearSelectedServer() {
        prefs.edit().remove(KEY_SELECTED_SERVER).apply()
    }
    
    /**
     * Get split tunnel excluded apps (apps with VPN disabled)
     */
    fun getSplitTunnelApps(): Set<String> {
        return prefs.getStringSet(KEY_SPLIT_TUNNEL_APPS, emptySet()) ?: emptySet()
    }
    
    /**
     * Save split tunnel excluded apps
     */
    fun saveSplitTunnelApps(appPackageNames: Set<String>) {
        prefs.edit().putStringSet(KEY_SPLIT_TUNNEL_APPS, appPackageNames).apply()
    }
    
    /**
     * Add app to split tunnel exclusion list
     */
    fun addSplitTunnelApp(packageName: String) {
        val apps = getSplitTunnelApps().toMutableSet()
        apps.add(packageName)
        saveSplitTunnelApps(apps)
    }
    
    /**
     * Remove app from split tunnel exclusion list
     */
    fun removeSplitTunnelApp(packageName: String) {
        val apps = getSplitTunnelApps().toMutableSet()
        apps.remove(packageName)
        saveSplitTunnelApps(apps)
    }
    
    /**
     * Check if app is in split tunnel exclusion list
     */
    fun isSplitTunnelApp(packageName: String): Boolean {
        return getSplitTunnelApps().contains(packageName)
    }
    
    /**
     * Get split tunnel enabled status
     */
    fun isSplitTunnelEnabled(): Boolean {
        return prefs.getBoolean(KEY_SPLIT_TUNNEL_ENABLED, false)
    }
    
    /**
     * Set split tunnel enabled status
     */
    fun setSplitTunnelEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SPLIT_TUNNEL_ENABLED, enabled).apply()
    }
}

