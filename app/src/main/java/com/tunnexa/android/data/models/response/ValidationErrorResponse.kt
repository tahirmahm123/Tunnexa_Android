package com.tunnexa.android.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * Model representing validation error response
 */
data class ValidationErrorResponse(
    @SerializedName("message")
    val message: String,
    
    @SerializedName("errors")
    val errors: Map<String, List<String>>?
)

