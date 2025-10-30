package com.tunnexa.ui.views.serverlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tunnexa.R
import com.tunnexa.ui.components.NavigationBar
import com.tunnexa.ui.components.NavigationBarType
import com.tunnexa.ui.components.SignalBars
import com.tunnexa.ui.theme.appGradientBrush
import com.tunnexa.ui.theme.DesignTokens
import com.tunnexa.utils.FlagMap

data class City(
    val name: String,
    val ip: String,
    val signalStrength: Int,
    val isSelected: Boolean = false,
    val isFavorite: Boolean = false
)

data class Server(
    val id: String,
    val country: String,
    val flagColor: Color,
    val cities: List<City>,
    val isFree: Boolean = true
)

val freeServers = listOf(
    Server(
        "us", "United States", Color(0xFFB22234),
        listOf(
            City("Miami", "123.123.23.21", 5, false, false),
            City("San Francisco", "123.123.23.21", 3, false, false),
            City("New York", "123.123.23.21", 5, true, false),
            City("California", "123.123.23.21", 2, false, false)
        ),
        true
    ),
    Server(
        "uk", "United Kingdom", Color(0xFF012169),
        listOf(
            City("London", "123.123.23.21", 4, false, false),
            City("Manchester", "123.123.23.21", 3, false, false)
        ),
        true
    ),
    Server(
        "de", "Germany", Color(0xFF000000),
        listOf(
            City("Berlin", "123.123.23.21", 5, false, false),
            City("Munich", "123.123.23.21", 4, false, false)
        ),
        true
    )
)

val premiumServers = listOf(
    Server(
        "fr", "France", Color(0xFF002654),
        listOf(
            City("Paris", "123.123.23.21", 4, false, false),
            City("Lyon", "123.123.23.21", 3, false, false)
        ),
        false
    ),
    Server(
        "jp", "Japan", Color(0xFFBC002D),
        listOf(
            City("Tokyo", "123.123.23.21", 3, false, false),
            City("Osaka", "123.123.23.21", 4, false, false)
        ),
        false
    )
)

@Composable
fun ServerListScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf("All Location") }
    var searchQuery by remember { mutableStateOf("") }
    var expandedCountries by remember { mutableStateOf(setOf<String>("us")) } // Expand United States by default
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = appGradientBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Top Navigation
            NavigationBar(
                type = NavigationBarType.SERVER_LIST,
                title = "Select Server",
                onBackClick = { navController.popBackStack() }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search for Country or City", color = Color.White.copy(alpha = 0.6f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DesignTokens.HorizontalPadding),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0x1AFFFFFF),
                    focusedContainerColor = Color(0x26FFFFFF),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Location Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DesignTokens.HorizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LocationTab(
                    text = "All Location",
                    isSelected = selectedTab == "All Location",
                    onClick = { selectedTab = "All Location" }
                )
                LocationTab(
                    text = "Favorites",
                    isSelected = selectedTab == "Favorites",
                    onClick = { selectedTab = "Favorites" }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Server List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = DesignTokens.HorizontalPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Free Location Section
                item {
                    Text(
                        text = "Free Location",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                items(freeServers) { server ->
                    ExpandableServerItem(
                        server = server,
                        isExpanded = expandedCountries.contains(server.id),
                        onToggleExpand = {
                            expandedCountries = if (expandedCountries.contains(server.id)) {
                                expandedCountries - server.id
                            } else {
                                expandedCountries + server.id
                            }
                        }
                    )
                }
                
                // Premium Location Section
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Premium Location",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                items(premiumServers) { server ->
                    ExpandableServerItem(
                        server = server,
                        isExpanded = expandedCountries.contains(server.id),
                        onToggleExpand = {
                            expandedCountries = if (expandedCountries.contains(server.id)) {
                                expandedCountries - server.id
                            } else {
                                expandedCountries + server.id
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) Color.White else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.Black else Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ExpandableServerItem(
    server: Server,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit
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
                    
                    Text(
                        text = server.country,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
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
                    CityItem(city = city, isLast = isLast)
                    if (!isLast) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.White.copy(alpha = 0.1f),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CityItem(city: City, isLast: Boolean = false) {
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
        onClick = { /* Handle city selection */ }
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
                    modifier = Modifier.size(20.dp)
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
