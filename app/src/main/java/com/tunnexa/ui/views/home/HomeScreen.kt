package com.tunnexa.ui.views.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tunnexa.R
import androidx.navigation.NavController
import com.tunnexa.navigation.Screen
import com.tunnexa.ui.theme.appGradientBrush
import com.tunnexa.ui.theme.DesignTokens
import com.tunnexa.ui.views.home.components.BottomNavigationBar
import com.tunnexa.ui.views.home.components.ConnectButton
import com.tunnexa.ui.views.home.components.ConnectionStatus
import com.tunnexa.ui.components.NavigationBar
import com.tunnexa.ui.components.NavigationBarType
import com.tunnexa.ui.views.home.components.FeedbackPopup
import com.tunnexa.ui.views.home.components.NavigationTab
import com.tunnexa.ui.views.home.components.ServerInfoChip
import com.tunnexa.ui.views.home.components.ServerSelectionCard
import com.tunnexa.ui.views.home.components.ShareDialog

/**
 * Main VPN home screen showing connection status and controls
 * Layout follows Material 3 standards with proper spacing from design tokens
 */
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeVM = viewModel()) {
    var selectedTab by remember { mutableStateOf(NavigationTab.HOME) }
    var isConnected by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    
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
                    country = "Canada",
                    ipAddress = "142.180.209.192"
                )
                
                Spacer(modifier = Modifier.height(25.dp))
                
                // Server Selection Card
                ServerSelectionCard(
                    countryName = "United State",
                    city = "New York City",
                    signalStrength = 5,
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
