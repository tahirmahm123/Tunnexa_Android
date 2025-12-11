package com.tunnexa.android.data.api

import com.tunnexa.android.data.models.request.DeviceRegisterRequest
import com.tunnexa.android.data.models.request.DeviceVerifyTokenRequest
import com.tunnexa.android.data.models.request.GetServersRequest
import com.tunnexa.android.data.models.response.DeviceRegisterResponse
import com.tunnexa.android.data.models.response.DeviceVerifyTokenResponse
import com.tunnexa.android.data.models.response.GetServersWrapperResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit API service interface for Tunnexa API endpoints
 */
interface TunnexaApiService {
    
    /**
     * Verify a device token
     * 
     * @param request Device verification request containing device_id and token
     * @return Response containing verification result
     */
    @POST("api/device/verify-token")
    suspend fun verifyDeviceToken(
        @Body request: DeviceVerifyTokenRequest
    ): Response<DeviceVerifyTokenResponse>
    
    /**
     * Register a new device
     * 
     * @param request Device registration request containing device_id and package_name
     * @return Response containing registration result
     */
    @POST("api/device/register")
    suspend fun registerDevice(
        @Body request: DeviceRegisterRequest
    ): Response<DeviceRegisterResponse>
    
    /**
     * Get VPN servers for a client device
     * 
     * @param request Request containing device_id and token
     * @return Response containing list of VPN servers wrapped in GetServersWrapperResponse
     */
    @POST("api/client/servers")
    suspend fun getServers(
        @Body request: GetServersRequest
    ): Response<GetServersWrapperResponse>
}

