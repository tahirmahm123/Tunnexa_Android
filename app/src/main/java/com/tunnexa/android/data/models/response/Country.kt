package com.tunnexa.android.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * Model representing a country with VPN servers
 */
data class Country(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("flag_url")
    val flagUrl: String,
    
    @SerializedName("country_code")
    val countryCode: String,
    
    @SerializedName("vpn_servers")
    val vpnServers: List<VpnServer>
)

