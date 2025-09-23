package com.example.littlelemonapp

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.core.content.edit

@Composable
fun Profile(navController: NavHostController, context: Context) {
    val sharedPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // Load user data
    val firstName = sharedPrefs.getString("firstName", "") ?: ""
    val lastName = sharedPrefs.getString("lastName", "") ?: ""
    val email = sharedPrefs.getString("email", "") ?: ""

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header with background color and centered logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                ,
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.height(100.dp)
                    .fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile info inside a card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Profile Information", fontSize = 20.sp, color = Color(0xFF333333))
                Divider()
                Text(text = "First name: $firstName", fontSize = 16.sp)
                Text(text = "Last name: $lastName", fontSize = 16.sp)
                Text(text = "Email: $email", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // push logout button to bottom

        // Log out button
        Button(
            onClick = {
                sharedPrefs.edit { clear() }
                navController.navigate(OnboardingDestination.route) {
                    popUpTo(HomeDestination.route) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14))
        ) {
            Text("Log out", color = Color.Black, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    val context = LocalContext.current
    Profile(navController = navController, context = context)
}
