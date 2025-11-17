package com.tunnexa.android.ui.views.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tunnexa.android.R
import com.tunnexa.android.ui.theme.BorderLight
import com.tunnexa.android.ui.theme.DesignTokens
import com.tunnexa.android.ui.theme.StatusConnected
import com.tunnexa.android.ui.theme.TextBlueGray
import com.tunnexa.android.ui.components.SignalBars
@Composable
fun ServerSelectionCard(
    countryName: String = "United State",
    city: String = "New York City",
    signalStrength: Int = 5,
    onClick: () -> Unit = {}
) {
    // Transparent diagonal background gradient
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2D334F).copy(alpha = 0.45f), // 45% opacity
            Color(0xFF3A3A70).copy(alpha = 0.43f)  // 43% opacity
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    // Gradient stroke
    val strokeGradient = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to Color.White.copy(alpha = 0.14f),
            0.4f to Color.White.copy(alpha = 0.0f),
            0.66f to Color.White.copy(alpha = 0.16f),
            1.0f to Color.White.copy(alpha = 0.0f),
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                spotColor = Color.Black.copy(alpha = 0.3f),
                ambientColor = Color.Black.copy(alpha = 0.3f)
            )
            .drawBehind {
                // Draw gradient stroke manually
                val strokeWidth = 1.5.dp.toPx()
                val halfStroke = strokeWidth / 2
                val rect = Size(size.width - strokeWidth, size.height - strokeWidth)
                drawRoundRect(
                    brush = strokeGradient,
                    size = rect,
                    topLeft = Offset(halfStroke, halfStroke),
                    cornerRadius = CornerRadius(18.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
            },
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent, // Important for parent transparency
        onClick = onClick
    ) {
        // Semi-transparent background allows image behind to peek through
        Box(
            modifier = Modifier
                .background(backgroundGradient, RoundedCornerShape(18.dp))
                .padding(
                    horizontal = DesignTokens.SpaceSmall,
                    vertical = 20.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(DesignTokens.SpaceSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Flag
                    Image(
                        painter = painterResource(id = R.drawable.flag_us),
                        contentDescription = "US Flag",
                        modifier = Modifier
                            .size(41.dp)
                            .clip(CircleShape)
                    )

                    // Country + City
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(DesignTokens.SpaceXSmall),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = countryName,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                            SignalBars(strength = signalStrength)
                        }
                        Text(
                            text = city,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextBlueGray
                        )
                    }
                }

                // Arrow
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF8FC9FF).copy(alpha = 0.13f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_right),
                        contentDescription = "Select server",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF101010)
@Composable
fun ServerSelectionCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        ServerSelectionCard()
    }
}