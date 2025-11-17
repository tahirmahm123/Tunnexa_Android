package com.tunnexa.android.ui.views.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunnexa.android.data.api.ApiManager
import com.tunnexa.android.data.api.ApiResult
import com.tunnexa.android.data.storage.SecurePreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
 * Handles device registration and token verification
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val apiManager: ApiManager,
    private val securePreferencesManager: SecurePreferencesManager
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
                if (response.success == false || response.valid != true) {
                    // Token is invalid or verification failed, register device again
                    registerDevice()
                } else {
                    // Token is valid, navigate to home
                    _uiState.value = SplashUiState.NavigateToHome
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
                _uiState.value = SplashUiState.NavigateToHome
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
}

