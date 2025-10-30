package com.tunnexa.ui.views.settings

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import com.tunnexa.ui.components.NavigationBar
import com.tunnexa.ui.components.NavigationBarType
import com.tunnexa.ui.theme.appGradientBrush
import com.tunnexa.ui.theme.DesignTokens
import com.tunnexa.R
import com.tunnexa.ui.theme.AppColors

@Composable
fun SettingsScreen(navController: NavController) {
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
                title = "Settings",
                onBackClick = { navController.popBackStack() }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Settings Content Card
            SettingsContentCard()
        }
    }
}

@Composable
private fun SettingsContentCard() {
    var darkMode by remember { mutableStateOf(true) }
    var killSwitch by remember { mutableStateOf(false) }
    var autoConnect by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DesignTokens.HorizontalPadding)
    ) {
        // Card container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2D334F).copy(alpha = 0.60f),
                            Color(0xFF3A3A70).copy(alpha = 0.50f)
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f), // top-left
                        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) // bottom-right
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color(0x33FFFFFF),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Dark Mode
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dark Mode",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TitleColor
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = if (darkMode) "On" else "Off",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.SubtitleColor
                    )
                }
                GradientToggle(isOn = darkMode) { darkMode = !darkMode }
            }

            Divider(modifier = Modifier.padding(vertical = 20.dp), color = Color(0x26FFFFFF))

            // Kill Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Kill Switch",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TitleColor
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "This option block internet without active VPN",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.SubtitleColor
                    )
                }
                GradientToggle(isOn = killSwitch) { killSwitch = !killSwitch }
            }

            Divider(modifier = Modifier.padding(vertical = 20.dp), color = Color(0x26FFFFFF))

            // Auto Connect
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Auto Connect",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppColors.TitleColor
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "This option block internet without active VPN",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.SubtitleColor
                    )
                }
                GradientToggle(isOn = autoConnect) { autoConnect = !autoConnect }
            }

            Divider(modifier = Modifier.padding(vertical = 20.dp), color = Color(0x26FFFFFF))

            // Split Tunnelling
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
                        text = "This option block internet without active VPN",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.SubtitleColor
                    )
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x33FFFFFF))
                        .clickable { /* navigate to split tunnelling */ },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.chevron_right),
                        contentDescription = "Details",
                        modifier = Modifier
                            .size(48.dp)
                    )
                }
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

