package com.tunnexa.android.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * Response model for device registration
 */
data class DeviceRegisterResponse(
    @SerializedName("status")
    val status: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("data")
    val data: DeviceRegisterData? = null,
    
    @SerializedName("errors")
    val errors: DeviceRegisterErrors? = null
)

/**
 * Data object containing device registration information
 */
data class DeviceRegisterData(
    @SerializedName("device_id")
    val deviceId: String,
    
    @SerializedName("token")
    val token: String
)

/**
 * Errors object in device registration response
 */
data class DeviceRegisterErrors(
    @SerializedName("code")
    val code: Int,
    
    @SerializedName("message")
    val message: String
)

