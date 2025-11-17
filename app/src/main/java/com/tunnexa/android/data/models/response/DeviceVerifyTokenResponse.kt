package com.tunnexa.android.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * Response model for device token verification
 */
data class DeviceVerifyTokenResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("valid")
    val valid: Boolean?
)

