package com.tunnexa.android.ui.views.settings

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import com.tunnexa.android.data.storage.SecurePreferencesManager
import com.tunnexa.android.ui.components.NavigationBar
import com.tunnexa.android.ui.components.NavigationBarType
import com.tunnexa.android.ui.theme.appGradientBrush
import com.tunnexa.android.ui.theme.DesignTokens

@Composable
fun SelectAppsScreen(navController: NavController) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val preferencesManager = remember { SecurePreferencesManager.getInstance(context) }
    
    // State for refreshing UI when apps change
    var refreshTrigger by remember { mutableStateOf(0) }
    
    // Get excluded apps from preferences
    val excludedApps = remember(refreshTrigger) {
        preferencesManager.getSplitTunnelApps()
    }
    
    // Get installed apps
    val installedApps = remember {
        getInstalledApps(packageManager)
    }
    
    // Filter apps into two lists
    val excludedAppsList = remember(excludedApps, installedApps) {
        installedApps.filter { excludedApps.contains(it.packageName) }
    }
    
    val availableAppsList = remember(excludedApps, installedApps) {
        installedApps.filter { !excludedApps.contains(it.packageName) }
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
                title = "Select apps",
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
                // VPN disabled for these apps section
                if (excludedAppsList.isNotEmpty()) {
                    item {
                        Text(
                            text = "VPN disabled for these apps",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    
                    item {
                        AppListCard(
                            apps = excludedAppsList,
                            onRemoveApp = { app ->
                                preferencesManager.removeSplitTunnelApp(app.packageName)
                                refreshTrigger++
                            },
                            showRemoveButton = true
                        )
                    }
                }
                
                // Select apps to disable VPN section
                item {
                    Text(
                        text = "Select apps to disable vpn",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(
                            top = if (excludedAppsList.isNotEmpty()) 8.dp else 0.dp,
                            bottom = 8.dp
                        )
                    )
                }
                
                item {
                    AppListCard(
                        apps = availableAppsList,
                        onAddApp = { app ->
                            preferencesManager.addSplitTunnelApp(app.packageName)
                            refreshTrigger++
                        },
                        showRemoveButton = false
                    )
                }
            }
        }
    }
}

@Composable
private fun AppListCard(
    apps: List<AppInfo>,
    onRemoveApp: ((AppInfo) -> Unit)? = null,
    onAddApp: ((AppInfo) -> Unit)? = null,
    showRemoveButton: Boolean
) {
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
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        apps.forEachIndexed { index, app ->
            AppListItem(
                app = app,
                onRemove = if (showRemoveButton && onRemoveApp != null) {
                    { onRemoveApp(app) }
                } else null,
                onAdd = if (!showRemoveButton && onAddApp != null) {
                    { onAddApp(app) }
                } else null
            )
            
            if (index < apps.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    thickness = 1.dp,
                    color = Color(0x26FFFFFF)
                )
            }
        }
    }
}

@Composable
private fun AppListItem(
    app: AppInfo,
    onRemove: (() -> Unit)? = null,
    onAdd: (() -> Unit)? = null
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
            // App Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x1AFFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                val iconBitmap = remember(app.icon) {
                    if (app.icon != null) {
                        try {
                            app.icon.toBitmap(40, 40).asImageBitmap()
                        } catch (e: Exception) {
                            null
                        }
                    } else {
                        null
                    }
                }
                
                if (iconBitmap != null) {
                    Image(
                        painter = BitmapPainter(iconBitmap),
                        contentDescription = app.appName,
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    // Fallback - show app name initial
                    Text(
                        text = app.appName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // App Name
            Text(
                text = app.appName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
        
        // Remove/Add Button
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0x33FFFFFF))
                .clickable {
                    onRemove?.invoke() ?: onAdd?.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            if (onRemove != null) {
                // X icon for remove
                Text(
                    text = "×",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            } else if (onAdd != null) {
                // + icon for add
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun getInstalledApps(packageManager: PackageManager): List<AppInfo> {
    val apps = mutableListOf<AppInfo>()
    
    try {
        val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        
        packages.forEach { packageInfo ->
            val applicationInfo = packageInfo.applicationInfo
            // Filter out system apps and this app itself
            if (applicationInfo != null && 
                ((applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 ||
                 (applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
            ) {
                if (packageInfo.packageName != "com.tunnexa.android") {
                    try {
                        val appName = packageManager.getApplicationLabel(applicationInfo).toString()
                        val icon = packageManager.getApplicationIcon(packageInfo.packageName)
                        
                        apps.add(
                            AppInfo(
                                packageName = packageInfo.packageName,
                                appName = appName,
                                icon = icon
                            )
                        )
                    } catch (e: Exception) {
                        // Skip apps that can't be loaded
                    }
                }
            }
        }
    } catch (e: Exception) {
        // Handle permission or other errors
    }
    
    // Sort by app name
    return apps.sortedBy { it.appName }
}

