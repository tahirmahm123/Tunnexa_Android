package com.tunnexa.android.ui.views.serverlist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tunnexa.android.R
import com.tunnexa.android.ui.components.SignalBars
import com.tunnexa.android.ui.views.serverlist.City

@Composable
fun CityItem(
    city: City,
    isLast: Boolean = false,
    onToggleFavorite: () -> Unit,
    onSelectServer: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = if (isLast) 14.dp else 0.dp,
            bottomEnd = if (isLast) 14.dp else 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x0DFFFFFF)
        ),
        onClick = onSelectServer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Favorite star icon
                Image(
                    painter = painterResource(
                        id = if (city.isFavorite) 
                            R.drawable.star_enabled 
                        else 
                            R.drawable.star_disabled
                    ),
                    contentDescription = if (city.isFavorite) "Favorite" else "Not favorite",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onToggleFavorite() }
                )
                
                Column {
                    Text(
                        text = city.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = "IP ${city.ip}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
            
            // Signal strength and selection
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SignalBars(city.signalStrength)
                
                // Selection indicator
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (city.isSelected) {
                        Image(
                            painter = painterResource(id = R.drawable.selected),
                            contentDescription = "Selected",
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.unselected_icon),
                            contentDescription = "Not selected",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

