package com.tunnexa.android.data.repository

import com.tunnexa.android.data.api.ApiManager
import com.tunnexa.android.data.api.ApiResult
import com.tunnexa.android.data.cache.ServerCacheManager
import com.tunnexa.android.data.models.response.GetServersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing server data with caching
 * Provides cache-first strategy: returns cached data immediately, then fetches fresh data in background
 */
@Singleton
class ServerRepository @Inject constructor(
    private val apiManager: ApiManager,
    private val cacheManager: ServerCacheManager
) {
    
    /**
     * Get servers with cache-first strategy
     * - Returns cached data immediately if available
     * - Fetches fresh data in background and updates cache
     * - Emits cached data first, then fresh data if different
     */
    fun getServers(): Flow<ServerResult> = flow {
        // Check if we have cached data
        val cachedServers = cacheManager.getCachedServers()
        
        if (cachedServers != null) {
            // Emit cached data immediately for instant loading
            emit(ServerResult.Success(cachedServers, fromCache = true))
            
            // Fetch fresh data in background
            when (val result = apiManager.getServers()) {
                is ApiResult.Success -> {
                    val freshServers = result.data
                    // Update cache with fresh data
                    cacheManager.saveServers(freshServers)
                    // Emit fresh data if it's different from cached
                    if (freshServers != cachedServers) {
                        emit(ServerResult.Success(freshServers, fromCache = false))
                    }
                }
                is ApiResult.Error -> {
                    // If fetch fails, we still have cached data, so just log the error
                    // Optionally emit error for UI to handle
                    emit(ServerResult.Error(result.message, hasCachedData = true))
                }
                is ApiResult.NetworkError -> {
                    // Network error - cached data is still available
                    emit(ServerResult.NetworkError(hasCachedData = true))
                }
            }
        } else {
            // No cache available, fetch from API
            when (val result = apiManager.getServers()) {
                is ApiResult.Success -> {
                    val servers = result.data
                    // Save to cache for next time
                    cacheManager.saveServers(servers)
                    emit(ServerResult.Success(servers, fromCache = false))
                }
                is ApiResult.Error -> {
                    emit(ServerResult.Error(result.message, hasCachedData = false))
                }
                is ApiResult.NetworkError -> {
                    emit(ServerResult.NetworkError(hasCachedData = false))
                }
            }
        }
    }
    
    /**
     * Force refresh servers from API (ignores cache)
     */
    suspend fun refreshServers(): ApiResult<GetServersResponse> {
        return when (val result = apiManager.getServers()) {
            is ApiResult.Success -> {
                // Update cache with fresh data
                cacheManager.saveServers(result.data)
                result
            }
            else -> result
        }
    }
    
    /**
     * Clear cached servers
     */
    fun clearCache() {
        cacheManager.clearCache()
    }
}

/**
 * Result wrapper for server repository operations
 */
sealed class ServerResult {
    data class Success(
        val data: GetServersResponse,
        val fromCache: Boolean
    ) : ServerResult()
    
    data class Error(
        val message: String,
        val hasCachedData: Boolean
    ) : ServerResult()
    
    data class NetworkError(
        val hasCachedData: Boolean
    ) : ServerResult()
}

