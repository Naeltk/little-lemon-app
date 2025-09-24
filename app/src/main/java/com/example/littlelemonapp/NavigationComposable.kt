package com.example.littlelemonapp

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MyNavigation(
    navController: NavHostController,
    startDestination: String,
    database: AppDatabase,
    context: Context
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(OnboardingDestination.route) {
            Onboarding(navController = navController, context = context)
        }

        composable(HomeDestination.route) {
            HomeScreen(database = database, navController = navController)
        }

        composable(ProfileDestination.route) {
            Profile(navController = navController, context = context)
        }
    }
}
