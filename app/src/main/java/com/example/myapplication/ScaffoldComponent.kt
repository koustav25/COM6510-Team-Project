package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.myapplication.screens.CategoryScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayScaffold(){
    var selectedScreenID by remember { mutableStateOf(ScreenID.HOME) }
    var title by remember { mutableStateOf("Home") }
    fun updatedSelectedID(id: Int, newTitle: String){
        selectedScreenID = id
        title = newTitle
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text( title) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ))
        },
        bottomBar = { NavigationComponents(selectedScreenID = selectedScreenID, updatedSelected = ::updatedSelectedID)},
        floatingActionButton = {
            FloatingActionButton(onClick = {  }) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Add, contentDescription = "ADD")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            CategoryScreen(categories = TodoCategoryData())
        }
    }
}