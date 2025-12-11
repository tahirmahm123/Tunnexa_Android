package com.tunnexa.android.ui.views.serverlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tunnexa.android.data.models.response.GetServersResponse
import com.tunnexa.android.data.storage.SecurePreferencesManager
import com.tunnexa.android.ui.components.NavigationBar
import com.tunnexa.android.ui.components.NavigationBarType
import com.tunnexa.android.ui.theme.appGradientBrush
import com.tunnexa.android.ui.theme.DesignTokens
import com.tunnexa.android.ui.views.serverlist.components.ExpandableServerItem
import com.tunnexa.android.ui.views.serverlist.components.LocationTab

data class City(
    val id: String,
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

/**
 * Maps API response to UI model
 */
fun mapApiResponseToServers(
    response: GetServersResponse,
    preferencesManager: SecurePreferencesManager
): Pair<List<Server>, List<Server>> {
    val freeServers = mutableListOf<Server>()
    val premiumServers = mutableListOf<Server>()
    
    val favoriteServers = preferencesManager.getFavoriteServers()
    val selectedServerId = preferencesManager.getSelectedServer()
    
    response.vpnServers.forEach { category ->
        val isFree = category.name.lowercase().contains("free")
        
        category.countries.forEach { country ->
            val cities = country.vpnServers.map { vpnServer ->
                val serverId = vpnServer.configId
                City(
                    id = serverId,
                    name = vpnServer.name,
                    ip = vpnServer.ipAddress,
                    signalStrength = normalizeSpeedScore(vpnServer.speedScore),
                    isSelected = serverId == selectedServerId,
                    isFavorite = favoriteServers.contains(serverId)
                )
            }
            
            val server = Server(
                id = country.countryCode.lowercase(),
                country = country.name,
                flagColor = Color.Transparent, // Not used, we use flag drawable instead
                cities = cities,
                isFree = isFree
            )
            
            if (isFree) {
                freeServers.add(server)
            } else {
                premiumServers.add(server)
            }
        }
    }
    
    return Pair(freeServers, premiumServers)
}

/**
 * Normalizes speed score (0-100) to signal strength (1-5)
 */
private fun normalizeSpeedScore(speedScore: Int): Int {
    return when {
        speedScore >= 80 -> 5
        speedScore >= 60 -> 4
        speedScore >= 40 -> 3
        speedScore >= 20 -> 2
        else -> 1
    }
}

@Composable
fun ServerListScreen(
    navController: NavController,
    viewModel: ServerViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf("All Location") }
    var searchQuery by remember { mutableStateOf("") }
    var expandedCountries by remember { mutableStateOf(setOf<String>()) }
    
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val preferencesManager = remember { SecurePreferencesManager.getInstance(context) }
    
    // State for refreshing UI when favorites/selection change
    var refreshTrigger by remember { mutableStateOf(0) }
    
    // Map API response to UI model when data is available
    val (freeServers, premiumServers) = remember(uiState, refreshTrigger) {
        val currentState = uiState
        when (currentState) {
            is ServerUiState.Success -> {
                mapApiResponseToServers(currentState.servers, preferencesManager)
            }
            else -> Pair(emptyList(), emptyList())
        }
    }
    
    // Callbacks for favorite and selection
    val onToggleFavorite: (String) -> Unit = { serverId ->
        preferencesManager.toggleFavoriteServer(serverId)
        refreshTrigger++
    }
    
    val onSelectServer: (String) -> Unit = { serverId ->
        preferencesManager.setSelectedServer(serverId)
        refreshTrigger++
        // Navigate back to home after selection
        navController.popBackStack()
    }
    
    // Filter servers based on search query and selected tab
    val filteredFreeServers = remember(freeServers, searchQuery, selectedTab, refreshTrigger) {
        var filtered = freeServers
        
        // Filter by favorites tab
        if (selectedTab == "Favorites") {
            filtered = filtered.map { server ->
                server.copy(
                    cities = server.cities.filter { it.isFavorite }
                )
            }.filter { it.cities.isNotEmpty() }
        }
        
        // Filter by search query
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { server ->
                server.country.contains(searchQuery, ignoreCase = true) ||
                server.cities.any { it.name.contains(searchQuery, ignoreCase = true) }
            }
        }
        
        filtered
    }
    
    val filteredPremiumServers = remember(premiumServers, searchQuery, selectedTab, refreshTrigger) {
        var filtered = premiumServers
        
        // Filter by favorites tab
        if (selectedTab == "Favorites") {
            filtered = filtered.map { server ->
                server.copy(
                    cities = server.cities.filter { it.isFavorite }
                )
            }.filter { it.cities.isNotEmpty() }
        }
        
        // Filter by search query
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { server ->
                server.country.contains(searchQuery, ignoreCase = true) ||
                server.cities.any { it.name.contains(searchQuery, ignoreCase = true) }
            }
        }
        
        filtered
    }
    
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
            val currentState = uiState
            when (currentState) {
                is ServerUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is ServerUiState.Error -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = DesignTokens.HorizontalPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                        item {
                            Text(
                                text = "Error: ${currentState.message}",
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        // Show cached data if available
                        if (currentState.hasCachedData && (filteredFreeServers.isNotEmpty() || filteredPremiumServers.isNotEmpty())) {
                item {
                    Text(
                        text = "Free Location",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                            items(filteredFreeServers) { server ->
                    ExpandableServerItem(
                        server = server,
                        isExpanded = expandedCountries.contains(server.id),
                        onToggleExpand = {
                            expandedCountries = if (expandedCountries.contains(server.id)) {
                                expandedCountries - server.id
                            } else {
                                expandedCountries + server.id
                            }
                                    },
                                    onToggleFavorite = onToggleFavorite,
                                    onSelectServer = onSelectServer
                    )
                }
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
                            items(filteredPremiumServers) { server ->
                    ExpandableServerItem(
                        server = server,
                        isExpanded = expandedCountries.contains(server.id),
                        onToggleExpand = {
                            expandedCountries = if (expandedCountries.contains(server.id)) {
                                expandedCountries - server.id
                            } else {
                                expandedCountries + server.id
                            }
                                    },
                                    onToggleFavorite = onToggleFavorite,
                                    onSelectServer = onSelectServer
                    )
                }
            }
        }
    }
                is ServerUiState.NetworkError -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Network Error",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge
                            )
        Text(
                                text = "Please check your internet connection",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Button(
                                onClick = { viewModel.loadServers() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Text("Retry", color = Color.Black)
                            }
                        }
                    }
                }
                is ServerUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = DesignTokens.HorizontalPadding),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        // Free Location Section
                        if (filteredFreeServers.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Free Location",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }
                            items(filteredFreeServers) { server ->
                                ExpandableServerItem(
                                    server = server,
                                    isExpanded = expandedCountries.contains(server.id),
                                    onToggleExpand = {
                                        expandedCountries = if (expandedCountries.contains(server.id)) {
                                            expandedCountries - server.id
                                        } else {
                                            expandedCountries + server.id
                                        }
                                    },
                                    onToggleFavorite = onToggleFavorite,
                                    onSelectServer = onSelectServer
                                )
                            }
                        }
                        
                        // Premium Location Section
                        if (filteredPremiumServers.isNotEmpty()) {
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
                            items(filteredPremiumServers) { server ->
                                ExpandableServerItem(
                                    server = server,
                                    isExpanded = expandedCountries.contains(server.id),
                                    onToggleExpand = {
                                        expandedCountries = if (expandedCountries.contains(server.id)) {
                                            expandedCountries - server.id
                                        } else {
                                            expandedCountries + server.id
                                        }
                                    },
                                    onToggleFavorite = onToggleFavorite,
                                    onSelectServer = onSelectServer
                                )
                            }
                        }
                        
                        // Empty state
                        if (filteredFreeServers.isEmpty() && filteredPremiumServers.isEmpty()) {
                            item {
                                Box(
            modifier = Modifier
                .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                    Text(
                                        text = if (searchQuery.isBlank()) "No servers available" else "No servers found",
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
                        }
                    }
                }
            }
        }
    }
}

