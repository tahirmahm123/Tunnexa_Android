package com.tunnexa.android.ui.views.serverlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunnexa.android.data.api.ApiResult
import com.tunnexa.android.data.models.response.Category
import com.tunnexa.android.data.models.response.GetServersResponse
import com.tunnexa.android.data.repository.ServerRepository
import com.tunnexa.android.data.repository.ServerResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State for Server loading
 */
sealed class ServerUiState {
    object Loading : ServerUiState()
    data class Success(
        val servers: GetServersResponse,
        val fromCache: Boolean
    ) : ServerUiState()
    data class Error(
        val message: String,
        val hasCachedData: Boolean
    ) : ServerUiState()
    object NetworkError : ServerUiState()
}

/**
 * ViewModel for managing server data with caching
 */
@HiltViewModel
class ServerViewModel @Inject constructor(
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ServerUiState>(ServerUiState.Loading)
    val uiState: StateFlow<ServerUiState> = _uiState.asStateFlow()

    private val _servers = MutableStateFlow<GetServersResponse?>(null)
    val servers: StateFlow<GetServersResponse?> = _servers.asStateFlow()

    init {
        loadServers()
    }

    /**
     * Load servers with cache-first strategy
     */
    fun loadServers() {
        viewModelScope.launch {
            serverRepository.getServers()
                .catch { e ->
                    _uiState.value = ServerUiState.Error(
                        e.message ?: "Unknown error",
                        hasCachedData = _servers.value != null
                    )
                }
                .collect { result ->
                    when (result) {
                        is ServerResult.Success -> {
                            _servers.value = result.data
                            _uiState.value = ServerUiState.Success(
                                servers = result.data,
                                fromCache = result.fromCache
                            )
                        }
                        is ServerResult.Error -> {
                            _uiState.value = ServerUiState.Error(
                                message = result.message,
                                hasCachedData = result.hasCachedData
                            )
                        }
                        is ServerResult.NetworkError -> {
                            _uiState.value = ServerUiState.NetworkError
                        }
                    }
                }
        }
    }

    /**
     * Force refresh servers from API
     */
    fun refreshServers() {
        viewModelScope.launch {
            _uiState.value = ServerUiState.Loading
            when (val result = serverRepository.refreshServers()) {
                is ApiResult.Success -> {
                    _servers.value = result.data
                    _uiState.value = ServerUiState.Success(
                        servers = result.data,
                        fromCache = false
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = ServerUiState.Error(
                        message = result.message ?: "Failed to refresh servers",
                        hasCachedData = _servers.value != null
                    )
                }
                is ApiResult.NetworkError -> {
                    _uiState.value = ServerUiState.NetworkError
                }
            }
        }
    }

    /**
     * Get all categories from servers
     */
    fun getCategories(): List<Category> {
        return _servers.value?.vpnServers ?: emptyList()
    }

    /**
     * Clear cache
     */
    fun clearCache() {
        serverRepository.clearCache()
        _servers.value = null
        _uiState.value = ServerUiState.Loading
    }
}

