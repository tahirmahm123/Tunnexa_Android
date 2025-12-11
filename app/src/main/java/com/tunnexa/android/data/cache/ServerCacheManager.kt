package com.tunnexa.android.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tunnexa.android.data.models.response.GetServersResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages caching of VPN servers data
 */
@Singleton
class ServerCacheManager @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        CACHE_PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val CACHE_PREFS_NAME = "tunnexa_server_cache"
        private const val KEY_SERVERS = "cached_servers"
        private const val KEY_CACHE_TIMESTAMP = "cache_timestamp"
    }
    
    /**
     * Save servers response to cache
     */
    fun saveServers(serversResponse: GetServersResponse) {
        val json = gson.toJson(serversResponse)
        prefs.edit()
            .putString(KEY_SERVERS, json)
            .putLong(KEY_CACHE_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }
    
    /**
     * Get cached servers response
     */
    fun getCachedServers(): GetServersResponse? {
        val json = prefs.getString(KEY_SERVERS, null) ?: return null
        
        return try {
            val type = object : TypeToken<GetServersResponse>() {}.type
            gson.fromJson<GetServersResponse>(json, type)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Check if cache exists
     */
    fun hasCachedServers(): Boolean {
        return prefs.contains(KEY_SERVERS)
    }
    
    /**
     * Get cache timestamp
     */
    fun getCacheTimestamp(): Long {
        return prefs.getLong(KEY_CACHE_TIMESTAMP, 0L)
    }
    
    /**
     * Clear cache
     */
    fun clearCache() {
        prefs.edit()
            .remove(KEY_SERVERS)
            .remove(KEY_CACHE_TIMESTAMP)
            .apply()
    }
    
    /**
     * Check if cache is stale (older than specified time in milliseconds)
     */
    fun isCacheStale(maxAgeMillis: Long = 24 * 60 * 60 * 1000): Boolean {
        val timestamp = getCacheTimestamp()
        if (timestamp == 0L) return true
        return (System.currentTimeMillis() - timestamp) > maxAgeMillis
    }
}

