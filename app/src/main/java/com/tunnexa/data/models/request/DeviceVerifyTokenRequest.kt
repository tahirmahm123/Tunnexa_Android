package com.tunnexa.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * Request model for device token verification
 */
data class DeviceVerifyTokenRequest(
    @SerializedName("device_id")
    val deviceId: String,
    
    @SerializedName("token")
    val token: String
)

