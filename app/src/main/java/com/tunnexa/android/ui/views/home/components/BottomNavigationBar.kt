package com.tunnexa.android.ui.views.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tunnexa.android.R
import com.tunnexa.android.ui.components.NavigationBar
import com.tunnexa.android.ui.theme.DesignTokens
import com.tunnexa.android.ui.theme.PrimaryGreen
import com.tunnexa.android.ui.views.home.HomeVM
import kotlinx.coroutines.launch

/**
 * Bottom navigation bar with 4 tabs
 * Note: Replace Material icons with Figma-exported icons
 * (ic_nav_home.svg, ic_nav_opinion.svg, ic_nav_rate.svg, ic_nav_share.svg)
 */
@Composable
fun BottomNavigationBar(
    vm: HomeVM = viewModel(),
    selectedTab: NavigationTab = NavigationTab.HOME,
    onTabSelected: (NavigationTab) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(83.dp),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 37.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationTab.entries.forEach { tab ->
                    BottomNavItem(
                        tab = tab,
                        isSelected = tab == selectedTab,
                        onClick = {
                            if (tab == NavigationTab.OPINION) {
                                vm.show()
                            }
                            onTabSelected(tab)
                        }
                    )
                }
            }
        }
        
        // Gradient border at the top with 4 stops at 30 degree angle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    brush = Brush.linearGradient(
                        // 30-degree angle: start from left, end at 30° from horizontal
                        start = Offset.Zero,
                        end = Offset(
                            x = 1000f, // Large value for full width
                            y = -kotlin.math.tan(kotlin.math.PI / 2).toFloat() * 1000f // 30° downward
                        ),
                        colorStops = arrayOf(
                            0.0f to Color.White.copy(alpha = 0.14f),  // 0% - White 14% opacity
                            0.40f to Color.White.copy(alpha = 0f),    // 40% - White 0% opacity
                            0.66f to Color.White.copy(alpha = 0.16f), // 66% - White 16% opacity
                            1.0f to Color.White.copy(alpha = 0f)      // 100% - White 0% opacity
                        )
                    )
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomNavItem(
    tab: NavigationTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tooltipState = rememberTooltipState()
    val coroutineScope = rememberCoroutineScope()
    
    // Tooltip content configuration for Opinion and Rate Us
    val tooltipConfig: Triple<String, String, String>? = when (tab) {
        NavigationTab.OPINION -> Triple(
            "Send Feedback",
            "We'd love to hear your opinion! Please let us know how we can improve.",
            "Send Feedback"
        )
        NavigationTab.RATE -> Triple(
            "Rate Us",
            "If you enjoy using our VPN, please take a moment to rate us on the Play Store. Your feedback helps us improve!",
            "Rate on Play Store"
        )
        NavigationTab.SHARE -> Triple(
            "Share",
            "Share Tunnexa with friends",
            "Share"
        )
        NavigationTab.HOME -> null
    }
    
    // Only show tooltip for Opinion and Rate Us
    if (tooltipConfig != null && (tab == NavigationTab.RATE || tab == NavigationTab.OPINION) || tab == NavigationTab.SHARE) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(spacingBetweenTooltipAndAnchor = 60.dp),
            state = tooltipState,
            tooltip = {
                RichTooltip(
                    title = { 
                        Text(
                            tooltipConfig!!.first,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        ) 
                    },
                    caretSize = DpSize(width = 50.dp, height = 25.dp),
                    action = {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    tooltipState.dismiss()
                                }
                                onClick()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(tooltipConfig!!.third)
                        }
                    }
                ) {
                    Text(
                        tooltipConfig!!.second,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(DesignTokens.SpaceXSmall),
                modifier = Modifier.clickable {
                    coroutineScope.launch {
                        tooltipState.show()
                    }
                    onClick()
                }
            ) {
                Icon(
                    painter = painterResource(id = tab.iconRes),
                    contentDescription = tab.label,
                    tint = if (isSelected) PrimaryGreen else Color.White,
                    modifier = Modifier.size(28.dp)
                )
                
                Text(
                    text = tab.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) PrimaryGreen else Color.White,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        // For Home and Share tabs, show simple clickable without tooltip
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DesignTokens.SpaceXSmall),
            modifier = Modifier.clickable(onClick = onClick)
        ) {
            Icon(
                painter = painterResource(id = tab.iconRes),
                contentDescription = tab.label,
                tint = if (isSelected) PrimaryGreen else Color.White,
                modifier = Modifier.size(28.dp)
            )
            
            Text(
                text = tab.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) PrimaryGreen else Color.White,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

enum class NavigationTab(
    val label: String,
    val iconRes: Int
) {
    HOME(
        label = "Home",
        iconRes = R.drawable.home_icon
    ),
    OPINION(
        label = "Opinion",
        iconRes = R.drawable.feedback
    ),
    RATE(
        label = "Rate Us",
        iconRes = R.drawable.rate_us
    ),
    SHARE(
        label = "Share",
        iconRes = R.drawable.share_us
    )
}
