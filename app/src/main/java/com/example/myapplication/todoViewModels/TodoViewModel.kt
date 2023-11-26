package com.example.myapplication.todoViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodoViewModel(app: Application) : AndroidViewModel(app) {
    private val context = getApplication<Application>().applicationContext
    private val dao = TodoDatabase.getDatabase(context).todoDao()

    val allTodos: Flow<List<Todo>> = dao.getAllTodos()
    val currentDateTodos: Flow<List<Todo>> = dao.getTodosByDate(LocalDate.now().toString())
    val importantTodos: Flow<List<Todo>> = dao.getImportantTodos()

    fun addTodo(title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addTodo(title, description)
        }
    }

    fun isEmpty(): Boolean = runBlocking{
        val todosList = allTodos.firstOrNull()
        todosList.isNullOrEmpty()
    }
}