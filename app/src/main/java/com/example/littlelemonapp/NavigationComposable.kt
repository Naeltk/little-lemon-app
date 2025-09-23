package com.example.littlelemonapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyNavigation(startDestination: String) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = startDestination) {

        // Onboarding screen
        composable(OnboardingDestination.route) {
            Onboarding(navController = navController, context = context)
        }

        // Home screen
        composable(HomeDestination.route) {
            Home(navController = navController)
        }

        // Profile screen
        composable(ProfileDestination.route) {
            Profile(navController = navController, context = context)
        }
    }
}
