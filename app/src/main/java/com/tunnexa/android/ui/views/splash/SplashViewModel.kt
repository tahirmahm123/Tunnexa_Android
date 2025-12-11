package com.tunnexa.android.ui.views.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunnexa.android.data.api.ApiManager
import com.tunnexa.android.data.api.ApiResult
import com.tunnexa.android.data.repository.ServerRepository
import com.tunnexa.android.data.repository.ServerResult
import com.tunnexa.android.data.storage.SecurePreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI State for Splash Screen
 */
sealed class SplashUiState {
    object Loading : SplashUiState()
    object NavigateToHome : SplashUiState()
    data class Error(val message: String) : SplashUiState()
}

/**
 * ViewModel for Splash Screen
 * Handles device registration, token verification, and server loading
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val apiManager: ApiManager,
    private val securePreferencesManager: SecurePreferencesManager,
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        checkDeviceAndAuthenticate()
    }

    /**
     * Check if device has token and authenticate accordingly
     */
    private fun checkDeviceAndAuthenticate() {
        viewModelScope.launch {
            try {
                if (securePreferencesManager.hasToken()) {
                    // Token exists, verify it
                    verifyDeviceToken()
                } else {
                    // No token, register device
                    registerDevice()
                }
            } catch (e: Exception) {
                _uiState.value = SplashUiState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    /**
     * Verify device token
     */
    private suspend fun verifyDeviceToken() {
        when (val result = apiManager.verifyDeviceToken()) {
            is ApiResult.Success -> {
                val response = result.data
                // Check if success is false or valid is not true
                if (!response.success || response.valid != true) {
                    // Token is invalid or verification failed, register device again
                    registerDevice()
                } else {
                    // Token is valid, load servers then navigate
                    loadServers()
                }
            }
            is ApiResult.Error -> {
                // If verification fails (401 or any error), register device
                // 401 Unauthorized specifically means token is invalid
                registerDevice()
            }
            is ApiResult.NetworkError -> {
                _uiState.value = SplashUiState.Error(
                    "Network error. Please check your internet connection."
                )
            }
        }
    }

    /**
     * Register device
     */
    private suspend fun registerDevice() {
        when (val result = apiManager.registerDevice()) {
            is ApiResult.Success -> {
                // Device registered successfully, token is stored automatically
                // Load servers then navigate
                loadServers()
            }
            is ApiResult.Error -> {
                _uiState.value = SplashUiState.Error(
                    result.message ?: "Failed to register device"
                )
            }
            is ApiResult.NetworkError -> {
                _uiState.value = SplashUiState.Error(
                    "Network error. Please check your internet connection."
                )
            }
        }
    }
    
    /**
     * Load servers and navigate to home
     * Uses cache-first strategy: loads cached data instantly, then fetches fresh data in background
     */
    private suspend fun loadServers() {
        try {
            // Get first result (cached data if available, or API result if no cache)
            val firstResult = serverRepository.getServers().first()
            
            when (firstResult) {
                is ServerResult.Success -> {
                    // Servers loaded (from cache or API), navigate to home immediately
                    _uiState.value = SplashUiState.NavigateToHome
                    
                    // Continue collecting in background to update cache with fresh data
                    // This ensures cache is updated for next app open
                    viewModelScope.launch {
                        serverRepository.getServers().collect {
                            // Just consume the flow to ensure background fetch completes
                            // Cache will be updated automatically
                        }
                    }
                }
                is ServerResult.Error -> {
                    // If we have cached data, still navigate (error is non-critical)
                    if (firstResult.hasCachedData) {
                        _uiState.value = SplashUiState.NavigateToHome
                    } else {
                        _uiState.value = SplashUiState.Error(
                            firstResult.message
                        )
                    }
                }
                is ServerResult.NetworkError -> {
                    // If we have cached data, still navigate (network error is non-critical)
                    if (firstResult.hasCachedData) {
                        _uiState.value = SplashUiState.NavigateToHome
                    } else {
                        _uiState.value = SplashUiState.Error(
                            "Network error. Please check your internet connection."
                        )
                    }
                }
            }
        } catch (e: Exception) {
            // If exception occurs, navigate anyway - cache might exist
            _uiState.value = SplashUiState.NavigateToHome
        }
    }
}

