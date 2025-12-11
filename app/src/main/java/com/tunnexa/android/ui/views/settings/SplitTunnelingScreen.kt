package com.tunnexa.android.ui.views.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tunnexa.android.R
import com.tunnexa.android.data.storage.SecurePreferencesManager
import com.tunnexa.android.navigation.Screen
import com.tunnexa.android.ui.components.NavigationBar
import com.tunnexa.android.ui.components.NavigationBarType
import com.tunnexa.android.ui.theme.appGradientBrush
import com.tunnexa.android.ui.theme.DesignTokens
import com.tunnexa.android.ui.theme.AppColors

@Composable
fun SplitTunnelingScreen(navController: NavController) {
    val context = LocalContext.current
    val preferencesManager = remember { SecurePreferencesManager.getInstance(context) }
    
    // State for refreshing UI when apps change
    var refreshTrigger by remember { mutableStateOf(0) }
    
    // Refresh when screen comes back into focus
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    LaunchedEffect(currentRoute) {
        if (currentRoute == Screen.SplitTunneling.route) {
            refreshTrigger++
        }
    }
    
    // Get excluded apps count from preferences
    val excludedApps = remember(refreshTrigger) {
        preferencesManager.getSplitTunnelApps()
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
                type = NavigationBarType.SETTINGS,
                title = "Split Tunnelling",
                onBackClick = { navController.popBackStack() }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = DesignTokens.HorizontalPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Split Tunnelling Toggle Card
                item {
                    SplitTunnelingToggleCard(
                        preferencesManager = preferencesManager,
                        refreshTrigger = { refreshTrigger++ }
                    )
                }
                
                // Select apps navigation item
                item {
                    SelectAppsNavigationCard(
                        excludedAppsCount = excludedApps.size,
                        onClick = { navController.navigate(Screen.SelectApps.route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectAppsNavigationCard(
    excludedAppsCount: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2D334F).copy(alpha = 0.60f),
                            Color(0xFF3A3A70).copy(alpha = 0.50f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color(0x33FFFFFF),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // App grid icon
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x33FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Simple grid representation with 4 small boxes
                        Column(
                            modifier = Modifier.size(16.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(1.dp))
                                )
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(1.dp))
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(1.dp))
                                )
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color.White.copy(alpha = 0.7f), RoundedCornerShape(1.dp))
                                )
                            }
                        }
                    }
                    
                    Column {
                        Text(
                            text = "Select apps",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = AppColors.TitleColor
                        )
                        if (excludedAppsCount > 0) {
                            Text(
                                text = "$excludedAppsCount app${if (excludedAppsCount == 1) "" else "s"} selected",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.SubtitleColor
                            )
                        }
                    }
                }
                
                Image(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = "Navigate",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun SplitTunnelingToggleCard(
    preferencesManager: SecurePreferencesManager,
    refreshTrigger: () -> Unit
) {
    // Get split tunnel status from preferences
    var splitTunnellingEnabled by remember { 
        mutableStateOf(preferencesManager.isSplitTunnelEnabled()) 
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF2D334F).copy(alpha = 0.60f),
                        Color(0xFF3A3A70).copy(alpha = 0.50f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color(0x33FFFFFF),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Split Tunnelling",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TitleColor
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Allows apps bypass the vpn",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.SubtitleColor
                )
            }
            GradientToggle(isOn = splitTunnellingEnabled) { 
                splitTunnellingEnabled = !splitTunnellingEnabled
                preferencesManager.setSplitTunnelEnabled(splitTunnellingEnabled)
                refreshTrigger()
            }
        }
    }
}

@Composable
private fun GradientToggle(isOn: Boolean, onToggle: () -> Unit) {
    val knobOffset by animateDpAsState(targetValue = if (isOn) 30.dp else 2.dp, label = "knobOffset")

    val inactiveBackground = Color(0xFF4B5563) // gray
    val activeBorder = Brush.linearGradient(listOf(Color(0xFF15E953), Color(0xFF5C67FF)))

    Box(
        modifier = Modifier
            .width(64.dp)
            .height(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isOn) Color.Transparent else inactiveBackground)
            .border(
                width = 1.dp,
                brush = if (isOn) activeBorder else Brush.linearGradient(listOf(Color(0x33FFFFFF), Color(0x33FFFFFF))),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onToggle() }
            .padding(horizontal = 2.dp, vertical = 4.dp)
    ) {
        // Knob
        Box(
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.CenterStart)
                .offset(x = knobOffset)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.95f)),
            contentAlignment = Alignment.Center
        ) {
            if (isOn) {
                Image(
                    painter = painterResource(id = R.drawable.check_box),
                    contentDescription = "On",
                    modifier = Modifier
                        .size(28.dp)
                )
            }
        }
    }
}
