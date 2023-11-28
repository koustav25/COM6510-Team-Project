package com.example.myapplication

import Favorites
import Settings
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Surface
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
import com.example.myapplication.screens.AddTodo
import com.example.myapplication.screens.CategoryScreen
import com.example.myapplication.screens.TodoDetail
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(){
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    var selectedScreenID by remember { mutableStateOf(ScreenID.HOME) }
    var title by remember { mutableStateOf("Home") }
    fun updatedSelectedID(id: Int, newTitle: String) {
        selectedScreenID = id
        title = newTitle

        //Bottom bar navigation
        currentScreen = when(id){
            ScreenID.HOME -> Screen.Home
            ScreenID.FAVORITES -> Screen.Favorites
            ScreenID.SETTING -> Screen.Settings
            ScreenID.ADD -> Screen.AddTodoScreen
            else -> currentScreen
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationComponents(
                selectedScreenID = selectedScreenID,
                updatedSelected = ::updatedSelectedID
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            var currentCategory by remember {
                mutableStateOf<TodoCategories?>(null)
            }
            MaterialTheme {
                when (currentScreen) {
                    Screen.Home -> CategoryScreen(categories = TodoCategoryData()) { selectedCategory ->
                        currentScreen = Screen.TodoScreen
                        currentCategory = selectedCategory
                    }
                    Screen.TodoScreen -> TodoDetail(selectedCategory = currentCategory) {
                        currentScreen = Screen.Home
                    }
                    Screen.AddTodoScreen -> AddTodo() {
                        currentScreen = Screen.TodoScreen
                        currentCategory = TodoCategories("All")
                    }
                    Screen.Settings -> {
                        Settings()
                    }
                    Screen.Favorites -> {
                        Favorites()
                    }
                }
            }
        }
    }
}

enum class Screen{
    Home, TodoScreen, AddTodoScreen, Favorites, Settings
}
