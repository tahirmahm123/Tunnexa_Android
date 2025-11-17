package com.tunnexa.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tunnexa.android.ui.views.home.HomeScreen
import com.tunnexa.android.ui.views.serverlist.ServerListScreen
import com.tunnexa.android.ui.views.settings.SettingsScreen
import com.tunnexa.android.ui.views.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Opinion : Screen("opinion")
    object RateUs : Screen("rate_us")
    object Share : Screen("share")
    object Settings : Screen("settings")
    object ServerList : Screen("server_list")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        
        composable(Screen.ServerList.route) {
            ServerListScreen(navController = navController)
        }
    }
}


