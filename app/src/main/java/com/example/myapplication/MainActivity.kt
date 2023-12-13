package com.example.myapplication

import Favorites
import Settings
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.function.Notification.Notification
//import com.example.myapplication.function.Notification.Notification
//import com.example.myapplication.function.Notification.NotificationScheduler
import com.example.myapplication.screens.AddSubtaskTodo
import com.example.myapplication.screens.AddTodo
import com.example.myapplication.screens.CategoryScreen
import com.example.myapplication.screens.Finished
import com.example.myapplication.screens.TodoDetail
import com.example.myapplication.todoViewModels.TodoViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
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
            var selectedTodoId by remember {
                mutableStateOf(0L)
            }
            MaterialTheme {
                when (currentScreen) {
                    Screen.Home -> CategoryScreen(categories = TodoCategoryData()) { selectedCategory ->
                        title = selectedCategory.todoCategories+" Todos"
                        currentScreen = Screen.TodoScreen
                        currentCategory = selectedCategory
                    }
                    Screen.TodoScreen -> TodoDetail(selectedCategory = currentCategory) { todoId ->
                        title = "Add subtask"
                        currentScreen = Screen.AddSubtask
                        selectedTodoId = todoId
                    }
                    Screen.AddTodoScreen -> AddTodo(templateTodos = TemplateTodosData()) {
                        currentScreen = Screen.TodoScreen
                        currentCategory = TodoCategories("All")
                        title = "All Todos"
                        selectedScreenID = ScreenID.HOME
                    }
                    Screen.AddSubtask -> AddSubtaskTodo(todoId = selectedTodoId){
                        currentScreen = Screen.TodoScreen
                        currentCategory = TodoCategories("All")
                        title = "All Todos"
                    }
                    Screen.Settings -> {
                        Settings()
                    }
                    Screen.Favorites -> {
                        Favorites(){todoId ->
                            currentScreen = Screen.AddSubtask
                            title = "Add subtask"
                            selectedTodoId = todoId
                            selectedScreenID = ScreenID.HOME
                        }
                    }
                }
            }
        }
    }
}

enum class Screen{
    Home, TodoScreen, AddTodoScreen, AddSubtask, Favorites, Settings
}
