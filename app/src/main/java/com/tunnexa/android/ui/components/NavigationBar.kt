package com.tunnexa.android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tunnexa.android.R
import com.tunnexa.android.ui.theme.DesignTokens

/**
 * Generic navigation bar component that adapts based on screen type
 * - Home: Logo + Menu on left, Globe on right
 * - Settings/ServerList: Back on left, Title in center, Globe on right
 */
enum class NavigationBarType {
    HOME,
    SETTINGS,
    SERVER_LIST
}

@Composable
fun NavigationBar(
    type: NavigationBarType,
    title: String = "",
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onGlobeClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = DesignTokens.SpaceSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        when (type) {
            NavigationBarType.HOME -> {
                IconButton(
                    onClick = onMenuClick,
//                    modifier = Modifier.clip(RoundedCornerShape(4.dp))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu_icon),
                        contentDescription = "Menu",
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
            
            NavigationBarType.SETTINGS,
            NavigationBarType.SERVER_LIST -> {
                // Left: Back button
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_icon),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(DesignTokens.IconSize)
                    )
                }
            }
        }
        
        if (type == NavigationBarType.HOME) {
            Image(
                painter = painterResource(id = R.drawable.logo_horizontal),
                contentDescription = "Tunnexa Logo",
                modifier = Modifier.height(31.dp)
            )
        } else if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Spacer for HOME to balance layout
            Spacer(modifier = Modifier.weight(1f))
        }
        
        // Right: Globe icon (all screens)
        IconButton(
            onClick = onGlobeClick,
//            modifier = Modifier.clip(RoundedCornerShape(4.dp))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.globe_icon),
                contentDescription = "Globe",
                tint = Color.White,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}
