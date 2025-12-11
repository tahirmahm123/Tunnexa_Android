package com.tunnexa.android.ui.views.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tunnexa.android.R
import androidx.navigation.NavController
import com.tunnexa.android.data.storage.SecurePreferencesManager
import com.tunnexa.android.navigation.Screen
import com.tunnexa.android.ui.theme.appGradientBrush
import com.tunnexa.android.ui.theme.DesignTokens
import com.tunnexa.android.data.models.response.GetServersResponse
import com.tunnexa.android.ui.views.serverlist.ServerViewModel
import com.tunnexa.android.ui.views.serverlist.ServerUiState
import com.tunnexa.android.ui.views.home.components.BottomNavigationBar
import com.tunnexa.android.ui.views.home.components.ConnectButton
import com.tunnexa.android.ui.views.home.components.ConnectionStatus
import com.tunnexa.android.ui.components.NavigationBar
import com.tunnexa.android.ui.components.NavigationBarType
import com.tunnexa.android.ui.views.home.components.FeedbackPopup
import com.tunnexa.android.ui.views.home.components.NavigationTab
import com.tunnexa.android.ui.views.home.components.ServerInfoChip
import com.tunnexa.android.ui.views.home.components.ServerSelectionCard
import com.tunnexa.android.ui.views.home.components.ShareDialog

/**
 * Main VPN home screen showing connection status and controls
 * Layout follows Material 3 standards with proper spacing from design tokens
 */
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeVM = viewModel(),
    serverViewModel: ServerViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(NavigationTab.HOME) }
    var isConnected by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    
    val serverUiState by serverViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val preferencesManager = remember { SecurePreferencesManager.getInstance(context) }
    
    // Get selected server info
    val selectedServerInfo = remember(serverUiState) {
        when (val state = serverUiState) {
            is ServerUiState.Success -> {
                val selectedServerId = preferencesManager.getSelectedServer()
                findSelectedServerInfo(state.servers, selectedServerId)
            }
            else -> null
        }
    }
    
    // Handle tab selection with navigation or dialogs
    val handleTabSelection = { tab: NavigationTab ->
        selectedTab = tab
        when (tab) {
            NavigationTab.HOME -> { /* Already on home */ }
            NavigationTab.OPINION -> { /* Show tooltip instead of dialog */ }
            NavigationTab.RATE -> { /* Show tooltip instead of dialog */ }
            NavigationTab.SHARE -> showShareDialog = true
        }
    }
    
    // Share dialog outside Box so it overlays properly
    if (showShareDialog) {
        ShareDialog(onDismiss = { showShareDialog = false })
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = appGradientBrush)
    ) {
        // Map background - covers 60% from top
        Image(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "Map Background",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .align(Alignment.TopCenter)
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Top Navigation
            NavigationBar(
                type = NavigationBarType.HOME,
                onMenuClick = { navController.navigate(Screen.Settings.route) }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 41.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(DesignTokens.SpaceXSmall)
            ) {
                // Connection Status
                ConnectionStatus(isConnected = isConnected)
                
                Spacer(modifier = Modifier.height(DesignTokens.SpaceXSmall))
                
                // Server Info Chip
                ServerInfoChip(
                    country = selectedServerInfo?.countryName ?: "Not Selected",
                    ipAddress = selectedServerInfo?.ipAddress ?: "N/A"
                )
                
                Spacer(modifier = Modifier.height(25.dp))
                
                // Server Selection Card
                ServerSelectionCard(
                    countryName = selectedServerInfo?.countryName ?: "Not Selected",
                    city = selectedServerInfo?.city ?: "Select a server",
                    signalStrength = selectedServerInfo?.signalStrength ?: 0,
                    countryCode = selectedServerInfo?.countryCode,
                    onClick = { navController.navigate(Screen.ServerList.route) }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Connect Button - centered horizontally
            ConnectButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                isConnected = isConnected,
                onClick = {
                    isConnected = !isConnected
                }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bottom Navigation
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = handleTabSelection
            )

        }

        if (viewModel.isVisible.value) {
            FeedbackPopup(
                onDismiss = { viewModel.hide() },
                onSubmit = { email, feedback ->
                    Log.d("Feedback", "Email: $email\nFeedback: $feedback")
                    viewModel.hide()
                }
            )
        }
    }
}

/**
 * Data class to hold selected server information
 */
private data class SelectedServerInfo(
    val countryName: String,
    val city: String,
    val ipAddress: String,
    val countryCode: String,
    val signalStrength: Int
)

/**
 * Helper function to find selected server information from the server response
 */
private fun findSelectedServerInfo(
    response: GetServersResponse,
    selectedServerId: String?
): SelectedServerInfo? {
    if (selectedServerId == null) return null
    
    response.vpnServers.forEach { category ->
        category.countries.forEach { country ->
            country.vpnServers.forEach { vpnServer ->
                if (vpnServer.configId == selectedServerId) {
                    // Normalize speed score to signal strength (1-5)
                    val signalStrength = when {
                        vpnServer.speedScore >= 80 -> 5
                        vpnServer.speedScore >= 60 -> 4
                        vpnServer.speedScore >= 40 -> 3
                        vpnServer.speedScore >= 20 -> 2
                        else -> 1
                    }
                    
                    return SelectedServerInfo(
                        countryName = country.name,
                        city = vpnServer.name,
                        ipAddress = vpnServer.ipAddress,
                        countryCode = country.countryCode.lowercase(),
                        signalStrength = signalStrength
                    )
                }
            }
        }
    }
    
    return null
}
