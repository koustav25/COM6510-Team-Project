package com.example.myapplication.todoViewModels

import androidx.lifecycle.ViewModel
import com.example.myapplication.todoDao.TodoDao
import com.example.myapplication.todoEntities.Todo

class SettingViewModel: ViewModel(){
    private lateinit var todoDao: TodoDao

    suspend fun addTodo(todo: Todo) {
        todoDao.insert(todo)
    }
}