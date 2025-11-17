package com.tunnexa.android.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * Response model for getting VPN servers
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

