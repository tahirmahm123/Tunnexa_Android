package com.tunnexa.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * Model representing API error response
 */
data class ApiError(
    @SerializedName("code")
    val code: Int
)

