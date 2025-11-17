package com.tunnexa.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * Response model for device registration
 */
data class DeviceRegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("token")
    val token: String? = null
)

