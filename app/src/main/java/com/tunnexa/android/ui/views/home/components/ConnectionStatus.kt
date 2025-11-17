package com.tunnexa.android.ui.views.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tunnexa.android.ui.theme.DesignTokens
import com.tunnexa.android.ui.theme.StatusDisconnected
import com.tunnexa.android.R
/**
 * Displays current VPN connection status
 * Note: Replace lock icon with Figma-exported ic_lock.svg
 */
@Composable
fun ConnectionStatus(
    isConnected: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DesignTokens.SpaceXXSmall)
    ) {
        // Lock Icon
        // TODO: Replace with painterResource(R.drawable.ic_lock)
        Icon(
            painter = painterResource(R.drawable.not_connected),
            contentDescription = if (isConnected) "Connected" else "Not Connected",
            tint = if (isConnected) MaterialTheme.colorScheme.primary else StatusDisconnected,
            modifier = Modifier.size(34.dp)
        )
        
        // Status Text
        Text(
            text = if (isConnected) "Connected" else "Not Connected",
            color = if (isConnected) MaterialTheme.colorScheme.primary else StatusDisconnected,
            fontSize = 25.sp, // Custom size for this specific component
            fontWeight = FontWeight.SemiBold
        )
    }
}
