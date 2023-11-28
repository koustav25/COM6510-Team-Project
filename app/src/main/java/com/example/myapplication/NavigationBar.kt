package com.example.myapplication

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

val navigationList = listOf(
    NavigationComponent("Home", Icons.Filled.Home, Icons.Outlined.Home, ScreenID.HOME),
    NavigationComponent("Add Todo", Icons.Filled.AddCircle, Icons.Outlined.Add, ScreenID.ADD),
    NavigationComponent("Favourites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, ScreenID.FAVORITES),
    NavigationComponent("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, ScreenID.SETTING)
)

@Composable
fun NavigationComponents(selectedScreenID: Int, updatedSelected: (screenID: Int, newTitle: String)-> Unit){
    NavigationBar {
        navigationList.forEach{ navigation ->
            NavigationBarItem(
                label = { Text(navigation.title)},
                icon = { navigation.iconStyled(selectedScreenID = selectedScreenID) },
                selected = (selectedScreenID == navigation.screenID),
                onClick = { updatedSelected(navigation.screenID, navigation.title) }
            )
        }
    }
}