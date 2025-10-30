package com.tunnexa.ui.views.home.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ServerInfoChip(
    country: String = "Canada",
    ipAddress: String = "142.180.209.192"
) {
    val shape = RoundedCornerShape(50)

    // Gradient border — same as your Figma design
    val gradientBorder = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.14f), // 0%
            Color.White.copy(alpha = 0.00f), // 40%
            Color.White.copy(alpha = 0.13f), // 83%
            Color.White.copy(alpha = 0.00f)  // 100%
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Surface(
        shape = shape,
        color = Color.Transparent,
        modifier = Modifier
            .shadow(
                elevation = 6.dp, // controls shadow size
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.25f), // soft glow
                spotColor = Color.Black.copy(alpha = 0.25f)
            )
            .border(width = 0.8.dp, brush = gradientBorder, shape = shape)
            .background(Color.Transparent, shape = shape)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = country,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "• $ipAddress",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}