package com.tunnexa.ui.views.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tunnexa.R
import com.tunnexa.ui.theme.appGradientBrush
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit = {}) {
    // Navigate to home after 3 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        onNavigateToHome()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = appGradientBrush)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Upper half with logo, title and subtitle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_with_text),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(200.dp)
                )

            }
            Spacer(modifier = Modifier.size(100.dp))
            // Bottom with loader centered
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                SplashLottieLoader(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 60.dp)
                )
            }
        }
    }
}

@Composable
fun SplashLottieLoader(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.splash_loader)
    )
    
    LottieAnimation(
        composition = composition,
        modifier = modifier,
        iterations = Int.MAX_VALUE
    )
}