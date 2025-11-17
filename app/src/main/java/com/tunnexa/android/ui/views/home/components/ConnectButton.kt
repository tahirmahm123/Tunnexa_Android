package com.tunnexa.android.ui.views.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tunnexa.android.R
import com.tunnexa.android.ui.theme.BackgroundDark
import com.tunnexa.android.ui.theme.BackgroundMedium
import com.tunnexa.android.ui.theme.DesignTokens
import com.tunnexa.android.ui.theme.TextGray
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextDecoration
import com.tunnexa.android.ui.theme.AppColors

@Composable
fun ConnectButton(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(DesignTokens.HorizontalPadding)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(264.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(243.dp)
                    .drawBehind {
                         // Top-left blue shadow
                        drawIntoCanvas { canvas ->
                            val paint = Paint()
                            paint.asFrameworkPaint().apply {
                                isAntiAlias = true
                                 color = Color(0xFF414C7B).toArgb()
                                 setShadowLayer(50.85f, -25f, -25f, Color(0xFF414C7B).toArgb())
                            }
                            canvas.drawCircle(
                                center = Offset(size.width / 2, size.height / 2),
                                radius = size.minDimension / 2,
                                paint = paint
                            )
                        }

                         // Bottom-right dark shadow
                        drawIntoCanvas { canvas ->
                            val paint = Paint()
                            paint.asFrameworkPaint().apply {
                                isAntiAlias = true
                                 color = Color(0xFF0A0E20).toArgb()
                                alpha = (255 * 0.81f).toInt() // 81% opacity
                                 setShadowLayer(50.85f, 25f, 25f, Color(0xFF0A0E20).toArgb())
                            }
                            canvas.drawCircle(
                                center = Offset(size.width / 2, size.height / 2),
                                radius = size.minDimension / 2,
                                paint = paint
                            )
                        }
                    }
                    .background(brush = Brush.linearGradient(
                        colors = listOf(
                            BackgroundDark,
                            BackgroundMedium
                        )
                    ), CircleShape)
            )

            // --- Main gradient circle ---
            Box(
                modifier = Modifier
                    .size(243.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                BackgroundDark,
                                BackgroundMedium
                            )
                        )
                    )
                    .clip(CircleShape)
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                // --- White or green ring ---
                Canvas(modifier = Modifier.size(153.dp)) {
                    drawCircle(
                        brush = if (isConnected) {
                            Brush.linearGradient(
                                colors = listOf(AppColors.ConnectColor1 , AppColors.ConnectColor2)
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFEBEBEB), Color.White.copy(alpha = 0.41f))
                            )
                        },
                        style = Stroke(width = 23.dp.toPx())
                    )
                }

                // --- Center icon ---
                Image(
                    painter = painterResource(id = if(isConnected) R.drawable.connected_icon else R.drawable.disconnected_icon),
                    contentDescription = "Disconnected",
                    modifier = Modifier.size(44.dp),
                )
            }
        }

        // --- Label ---
        Text(
            text = if (isConnected) "Tap To Disconnect" else "Tap To Connect",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isConnected) Color.White.copy(alpha = 0.85f) else TextGray,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF0000)
@Composable
fun ConnectButtonDisconnectedPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ConnectButton(
            isConnected = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun ConnectButtonConnectedPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ConnectButton(
            isConnected = true,
            onClick = {}
        )
    }
}