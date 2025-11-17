package com.tunnexa.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * Model representing a VPN server
 */
data class VpnServer(
    @SerializedName("priority")
    val priority: Int,
    
    @SerializedName("config_id")
    val configId: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("lat")
    val latitude: String,
    
    @SerializedName("long")
    val longitude: String,
    
    @SerializedName("speed_score")
    val speedScore: Int,
    
    @SerializedName("ip_address")
    val ipAddress: String,
    
    @SerializedName("is_locked")
    val isLocked: Boolean,
    
    @SerializedName("recommended_vpn")
    val recommendedVpn: String,
    
    @SerializedName("vpn_type")
    val vpnType: List<String>
)

