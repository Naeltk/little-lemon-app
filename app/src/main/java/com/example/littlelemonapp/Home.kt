package com.example.littlelemonapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun HomeScreen(database: AppDatabase, navController: NavHostController) {
    val menuItems by database.menuDao().getAllMenuItemsLive().observeAsState(emptyList())
    var searchPhrase by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val filteredItems = menuItems
        .filter { item ->
            (searchPhrase.isBlank() ||
                    item.title.contains(searchPhrase, ignoreCase = true)
                    )
        }
        .filter { item ->
            (selectedCategory == null || item.category.equals(selectedCategory, ignoreCase = true))
        }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(navController)
        HeroSection(searchPhrase=searchPhrase, onSearchChange = { searchPhrase = it })
        CategoriesRow( selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                selectedCategory = if (selectedCategory == category) null else category
            })
        MenuItems(filteredItems)
    }
}

@Composable
fun TopBar(navController: NavHostController) {    Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .background(Color(0xFFFFFFFF))
        .padding(horizontal = 16.dp),
    contentAlignment = Alignment.Center
) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "App Logo",
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxSize(0.8f)
    )

    Image(
        painter = painterResource(id = R.drawable.profile),
        contentDescription = "Profile",
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .size(60.dp)
            .clip(CircleShape)
            .clickable { navController.navigate(ProfileDestination.route) }
    )
}
}

@Composable
fun SearchBar(
    searchPhrase: String,
    onSearchPhraseChange: (String) -> Unit
) {
    TextField(
        value = searchPhrase,
        onValueChange = onSearchPhraseChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Enter Search Phrase") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun CategoriesRow(
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    Column {
        Text(
            text = "Order for Delivery",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Starters", "Mains", "Desserts", "Drinks").forEach { category ->
                val isSelected = selectedCategory == category
                Button(
                    onClick = { onCategorySelected(category) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFFF4CE14) else Color.LightGray,
                        contentColor = if (isSelected) Color.Black else Color.DarkGray
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        Divider(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            thickness = 1.dp,
            color = Color.LightGray
        )
    }
}
@Composable
fun HeroSection(searchPhrase: String, onSearchChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .background(Color(0xFF495E57))
            .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = "Little Lemon",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF4CE14)
        )
        Text(
            text = "Chicago",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFEDEFEE)
        )
        Row (horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 20.dp)){
            Text(
                text = "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist",
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(bottom = 28.dp, end = 20.dp)
                    .fillMaxWidth(0.6f),
                color = Color(0xFFEDEFEE)
            )

            Image(
                painter = painterResource(id = R.drawable.hero_image),
                contentDescription = "Hero Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
        SearchBar(searchPhrase=searchPhrase, onSearchPhraseChange = onSearchChange)
    }
}
@Composable
fun MenuItems(menuItems: List<MenuItemEntity>) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(menuItems) { item ->
            MenuItemCard(item)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MenuItemCard(item: MenuItemEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(item.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(item.description, fontSize = 14.sp, maxLines = 2)
            Text("$${item.price}", fontSize = 14.sp, fontWeight = FontWeight.Medium)

        }

        GlideImage(
            model = item.image,
            contentDescription = item.title,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
    }
    Divider(  modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        thickness = 1.dp,
        color = Color.LightGray)
}
