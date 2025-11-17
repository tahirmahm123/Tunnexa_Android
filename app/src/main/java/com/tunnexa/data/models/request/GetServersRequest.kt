package com.tunnexa.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * Request model for getting VPN servers
 */
data class GetServersRequest(
    @SerializedName("device_id")
    val deviceId: String,
    
    @SerializedName("token")
    val token: String
)

