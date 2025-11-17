package com.tunnexa.android.ui.views.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tunnexa.android.ui.theme.DesignTokens

/**
 * StatusBar component displaying time and system icons
 * Note: Replace Material icons with Figma-exported icons
 */
@Composable
fun StatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = DesignTokens.HorizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Time
        Text(
            text = "9:12",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(DesignTokens.SpaceXXSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: Replace with Figma-exported icons
            // For now using placeholders
            // Icon(painter = painterResource(R.drawable.ic_signal), ...)
            // Icon(painter = painterResource(R.drawable.ic_wifi), ...)
            BatteryIndicator()
        }
    }
}

@Composable
private fun BatteryIndicator() {
    Box(
        modifier = Modifier
            .width(24.dp)
            .height(11.dp)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.35f),
                shape = RoundedCornerShape(2.dp)
            )
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.8f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(1.dp)
                )
        )
    }
}
