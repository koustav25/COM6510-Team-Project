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
    val scheduledTodos: Flow<List<Todo>> = dao.getScheduledTodos(LocalDate.now().toString())
    val favoriteTodos: Flow<List<Todo>> = dao.getFavoriteTodos()
    val todosInBin: Flow<List<Todo>> = dao.getDeletedTodos()

    fun addTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(todo)
        }
    }

    fun isEmpty(): Boolean = runBlocking{
        val todosList = allTodos.firstOrNull()
        todosList.isNullOrEmpty()
    }

    fun isImportantEmpty(): Boolean = runBlocking {
        val todosList = importantTodos.firstOrNull()
        todosList.isNullOrEmpty()
    }

    fun isScheduledEmpty(): Boolean = runBlocking{
        val todosList = scheduledTodos.firstOrNull()
        todosList.isNullOrEmpty()
    }

    fun setFavourite(todoItemId:Long, isFavClicked:Boolean ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.setFavourite(todoItemId, isFavClicked)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setImportant(todoItemId:Long, isImportant:Boolean ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.setImportant(todoItemId, isImportant)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun isFavoritesEmpty(): Boolean = runBlocking{
        val todosList = favoriteTodos.firstOrNull()
        todosList.isNullOrEmpty()
    }

    suspend fun deleteSelectedTodos(id:Long) {
        dao.delete(id)
    }
}