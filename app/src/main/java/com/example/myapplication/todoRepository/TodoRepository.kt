package com.example.myapplication.todoRepository

import com.example.myapplication.todoDao.TodoDao
import com.example.myapplication.todoEntities.Todo

class TodoRepository() {

    lateinit var todoDao: TodoDao
    suspend fun addTodo(todo: Todo) {
        todoDao.insert(todo)
    }
}