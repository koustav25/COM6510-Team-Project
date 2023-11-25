package com.example.myapplication.todoViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoViewModel(app: Application) : AndroidViewModel(app) {
    private val context = getApplication<Application>().applicationContext
    private val dao = TodoDatabase.getDatabase(context).todoDao()

    val allTodos: Flow<List<Todo>> = dao.getAllTodos()
    fun addTodo(title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addTodo(title, description)
        }
    }
}