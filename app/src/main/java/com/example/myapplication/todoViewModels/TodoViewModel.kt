package com.example.myapplication.todoViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
//import com.example.myapplication.screens.SortOption
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.Todo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import android.util.Log


class TodoViewModel(app: Application) : AndroidViewModel(app) {
    private val context = getApplication<Application>().applicationContext
    private val dao = TodoDatabase.getDatabase(context).todoDao()

    val allTodos: Flow<List<Todo>> = dao.getAllTodos()
    val currentDateTodos: Flow<List<Todo>> = dao.getTodosByDate(LocalDate.now().toString())
    val importantTodos: Flow<List<Todo>> = dao.getImportantTodos()
    val scheduledTodos: Flow<List<Todo>> = dao.getScheduledTodos(LocalDate.now().toString())
    val favoriteTodos: Flow<List<Todo>> = dao.getFavoriteTodos()
    val todosInBin: Flow<List<Todo>> = dao.getDeletedTodos()
    val finishedTodos: Flow<List<Todo>> = dao.getFinishedTodos()
    val latestDate: Flow<List<Todo>> = dao.getLatestDate()

    suspend fun addTodo(todo: Todo): Long {
        val deferred = CompletableDeferred<Long>()
        viewModelScope.launch(Dispatchers.IO) {
            val insertedId = dao.insert(todo = todo)
            deferred.complete(insertedId)
        }
        return deferred.await()
    }

    fun updateTodo(todo:Todo){
        viewModelScope.launch(Dispatchers.IO){
            dao.update(todo)
        }
    }

    fun sentToBin(id:Long){
        viewModelScope.launch(Dispatchers.IO){
            dao.delete(id)
        }
    }

    fun isBinEmpty(): Boolean = runBlocking{
        val todosList = todosInBin.firstOrNull()
        todosList.isNullOrEmpty()
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

    fun setFinished(todoItemId:Long, isFinished:Boolean ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dao.setFinished(todoItemId, isFinished)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun isFinishedEmpty(): Boolean = runBlocking {
        val todosList = finishedTodos.firstOrNull()
        todosList.isNullOrEmpty()
    }


    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            val todosList = dao.getAllTodos().firstOrNull() ?: emptyList()
            _todos.postValue(todosList)
        }
    }

    private val _todos = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>> = _todos

    fun sortTodosByDate() {
        Log.d("TodoViewModel", "Sorting by date")
        _todos.value = _todos.value?.sortedBy { it.scheduledDate }
    }
    fun sortTodosByTitle() {
        Log.d("TodoViewModel", "Sorting by title")
        _todos.value = _todos.value?.sortedBy { it.title }
    }


}