package com.tunnexa.data.api

import android.content.Context
import com.tunnexa.data.models.request.DeviceRegisterRequest
import com.tunnexa.data.models.request.DeviceVerifyTokenRequest
import com.tunnexa.data.models.request.GetServersRequest
import com.tunnexa.data.models.response.DeviceRegisterResponse
import com.tunnexa.data.models.response.DeviceVerifyTokenResponse
import com.tunnexa.data.models.response.GetServersResponse
import com.tunnexa.data.storage.SecurePreferencesManager
import com.tunnexa.network.NetworkConfig
import retrofit2.HttpException
import java.io.IOException

/**
 * Result wrapper for API responses
 */
sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
    object NetworkError : ApiResult<Nothing>()
}

/**
 * API Manager class to handle all API calls with proper error handling
 * Automatically manages device ID and token storage
 */
class ApiManager private constructor(context: Context) {
    
    private val apiService: TunnexaApiService = NetworkConfig.apiService
    private val preferencesManager: SecurePreferencesManager = SecurePreferencesManager.getInstance(context)
    private val packageName: String = context.packageName
    
    companion object {
        @Volatile
        private var INSTANCE: ApiManager? = null
        
        fun getInstance(context: Context): ApiManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
    
    /**
     * Verify a device token
     * Automatically retrieves device ID and token from secure storage
     * 
     * @return ApiResult containing the verification response or error
     */
    suspend fun verifyDeviceToken(): ApiResult<DeviceVerifyTokenResponse> {
        val deviceId = preferencesManager.getDeviceId()
        val token = preferencesManager.getToken()
        
        if (token == null) {
            return ApiResult.Error("No token found. Please register device first.")
        }
        
        return try {
            val request = DeviceVerifyTokenRequest(
                deviceId = deviceId,
                token = token
            )
            val response = apiService.verifyDeviceToken(request)
            
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string() 
                    ?: "Failed to verify device token"
                ApiResult.Error(
                    message = errorMessage,
                    code = response.code()
                )
            }
        } catch (e: HttpException) {
            ApiResult.Error(
                message = e.message ?: "HTTP error occurred",
                code = e.code()
            )
        } catch (e: IOException) {
            ApiResult.NetworkError
        } catch (e: Exception) {
            ApiResult.Error(
                message = e.message ?: "An unexpected error occurred"
            )
        }
    }
    
    /**
     * Register a new device
     * Automatically retrieves device ID and uses app's package name
     * Stores token from response securely
     * 
     * @return ApiResult containing the registration response or error
     */
    suspend fun registerDevice(): ApiResult<DeviceRegisterResponse> {
        val deviceId = preferencesManager.getDeviceId()
        
        return try {
            val request = DeviceRegisterRequest(
                deviceId = deviceId,
                packageName = packageName
            )
            val response = apiService.registerDevice(request)
            
            if (response.isSuccessful && response.body() != null) {
                val responseBody = response.body()!!
                
                // Store token if provided in response
                responseBody.token?.let { token ->
                    preferencesManager.saveToken(token)
                }
                
                ApiResult.Success(responseBody)
            } else {
                val errorMessage = response.errorBody()?.string() 
                    ?: "Failed to register device"
                ApiResult.Error(
                    message = errorMessage,
                    code = response.code()
                )
            }
        } catch (e: HttpException) {
            ApiResult.Error(
                message = e.message ?: "HTTP error occurred",
                code = e.code()
            )
        } catch (e: IOException) {
            ApiResult.NetworkError
        } catch (e: Exception) {
            ApiResult.Error(
                message = e.message ?: "An unexpected error occurred"
            )
        }
    }
    
    /**
     * Get VPN servers for a client device
     * Automatically retrieves device ID and token from secure storage
     * 
     * @return ApiResult containing the servers response or error
     */
    suspend fun getServers(): ApiResult<GetServersResponse> {
        val deviceId = preferencesManager.getDeviceId()
        val token = preferencesManager.getToken()
        
        if (token == null) {
            return ApiResult.Error("No token found. Please register device first.")
        }
        
        return try {
            val request = GetServersRequest(
                deviceId = deviceId,
                token = token
            )
            val response = apiService.getServers(request)
            
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string() 
                    ?: "Failed to get servers"
                ApiResult.Error(
                    message = errorMessage,
                    code = response.code()
                )
            }
        } catch (e: HttpException) {
            ApiResult.Error(
                message = e.message ?: "HTTP error occurred",
                code = e.code()
            )
        } catch (e: IOException) {
            ApiResult.NetworkError
        } catch (e: Exception) {
            ApiResult.Error(
                message = e.message ?: "An unexpected error occurred"
            )
        }
    }
}

