package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                    App() //Code For Navigation
//                    AddTodo() //For adding the todo to the database
                    //TodoDetail() //{Here in this screen we may need to add the Navigation Bar in the Scaffold}
                    //DisplayScaffold()
                    //CategoryScreen(categories = TodoCategoryData())
                }
            }
        }
    }
}

@Composable
fun App(){
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    MaterialTheme{
        when(currentScreen){
            Screen.Home -> CategoryScreen(categories = TodoCategoryData()) {
               currentScreen = Screen.TodoScreen
            }
            Screen.TodoScreen -> TodoDetail {
                currentScreen = Screen.Home
            }
        }
    }
}

enum class Screen{
    Home, TodoScreen
}
