package com.example.littlelemonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init Room database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "little_lemon_db"
        ).build()

        // fetch and save data into DB
        lifecycleScope.launch {
            val menuItemsNetwork = fetchMenu()
            val menuItemsEntity = menuItemsNetwork.map {
                MenuItemEntity(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    image = it.image,
                    category = it.category
                )
            }
            database.menuDao().insertAll(menuItemsEntity)
        }

        setContent {
            val navController = rememberNavController()

            // check if user is registered
            val startDestination = if (userIsRegistered()) {
                HomeDestination.route
            } else {
                OnboardingDestination.route
            }

            MyNavigation(
                navController = navController,
                startDestination = startDestination,
                database = database,
                context=this
            )
        }
    }

    private fun userIsRegistered(): Boolean {
        val prefs = getSharedPreferences("little_lemon_prefs", MODE_PRIVATE)
        val firstName = prefs.getString("firstName", null)
        val lastName = prefs.getString("lastName", null)
        val email = prefs.getString("email", null)
        return !firstName.isNullOrBlank() && !lastName.isNullOrBlank() && !email.isNullOrBlank()
    }
}
