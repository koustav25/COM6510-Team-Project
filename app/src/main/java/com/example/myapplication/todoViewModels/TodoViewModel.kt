package com.example.myapplication.todoViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.todoEntities.Todo
import com.example.myapplication.todoRepository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {
    private val repository = TodoRepository()

    fun addTodo(title: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(Todo(title = title, description = description))
        }
    }
}