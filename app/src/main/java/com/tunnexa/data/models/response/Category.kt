package com.tunnexa.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * Model representing a server category
 */
data class Category(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("countries")
    val countries: List<Country>
)

