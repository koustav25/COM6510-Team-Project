package com.example.myapplication

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationComponent(
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val screenID: Int
) {

    @Composable
    fun iconStyled(selectedScreenID: Int) {
        if (selectedScreenID == screenID)
            Icon(filledIcon, contentDescription = title)
        else
            Icon(outlinedIcon, contentDescription = title)
    }

}