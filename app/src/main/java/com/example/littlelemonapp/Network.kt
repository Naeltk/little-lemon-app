package com.example.littlelemonapp

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

// -------------------
// Network Data Models
// -------------------
@Serializable
data class MenuNetwork(
    val menu: List<MenuItemNetwork>
)

@Serializable
data class MenuItemNetwork(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val image: String,
    val category: String
)

// -------------------
// Ktor Client
// -------------------
val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}

// -------------------
// Fetch Menu Function
// -------------------
suspend fun fetchMenu(): List<MenuItemNetwork> {
    val url =
        "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"

    // Fetch response as text to avoid content-type issues
    val responseText: String = httpClient.get(url).bodyAsText()

    // Decode manually
    val menuNetwork = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }.decodeFromString<MenuNetwork>(responseText)

    return menuNetwork.menu
}
