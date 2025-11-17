package com.tunnexa.android.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * Request model for device registration
 */
data class DeviceRegisterRequest(
    @SerializedName("device_id")
    val deviceId: String,
    
    @SerializedName("package_name")
    val packageName: String
)

