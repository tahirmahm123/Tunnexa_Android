package com.tunnexa.android.ui.views.serverlist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tunnexa.android.R
import com.tunnexa.android.ui.views.serverlist.City
import com.tunnexa.android.ui.views.serverlist.Server
import com.tunnexa.android.ui.views.serverlist.components.CityItem
import com.tunnexa.android.utils.FlagMap

@Composable
fun ExpandableServerItem(
    server: Server,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onSelectServer: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Country header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isExpanded) 0.dp else 14.dp,
                bottomEnd = if (isExpanded) 0.dp else 14.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x1AFFFFFF)
            ),
            onClick = onToggleExpand
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
                    // Flag
                    Image(
                        painter = painterResource(id = FlagMap.flags[server.id] ?: R.drawable.flag_us),
                        contentDescription = "${server.country} Flag",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                    
                    Column {
                        Text(
                            text = server.country,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            text = "${server.cities.size} Locations",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
                
                // Expand/Collapse button
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x26FFFFFF))
                        .clickable { onToggleExpand() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.chevron_down),
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(if (isExpanded) 180f else 0f)
                    )
                }
            }
        }
        
        // Expandable cities
        if (isExpanded) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                server.cities.forEachIndexed { index, city ->
                    val isLast = index == server.cities.size - 1
                    CityItem(
                        city = city,
                        isLast = isLast,
                        onToggleFavorite = { onToggleFavorite(city.id) },
                        onSelectServer = { onSelectServer(city.id) }
                    )
                    if (!isLast) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 1.dp,
                            color = Color.White.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

