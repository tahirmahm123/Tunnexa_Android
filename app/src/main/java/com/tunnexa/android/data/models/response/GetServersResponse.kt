package com.tunnexa.android.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * Wrapper response model for getting VPN servers (outer structure)
 */
data class GetServersWrapperResponse(
    @SerializedName("status")
    val status: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("data")
    val data: GetServersResponse,
    
    @SerializedName("errors")
    val errors: ApiError
)

/**
 * Response model for getting VPN servers (inner data structure)
 */
data class GetServersResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("user_location")
    val userLocation: List<Any>,
    
    @SerializedName("default_server")
    val defaultServer: String?,
    
    @SerializedName("vpn_servers")
    val vpnServers: List<Category>,
    
    @SerializedName("error")
    val error: ApiError
)

