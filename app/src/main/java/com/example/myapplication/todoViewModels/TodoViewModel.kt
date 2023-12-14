package com.example.myapplication.todoViewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.Todo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate


class TodoViewModel(app: Application) : AndroidViewModel(app) {
    private val context = getApplication<Application>().applicationContext
    private val dao = TodoDatabase.getDatabase(context).todoDao()

    val allTodos: Flow<List<Todo>> = dao.getAllTodos()
    val allTableTodos: Flow<List<Todo>> = dao.getAllTableTodos()
    val currentDateTodos: Flow<List<Todo>> = dao.getTodosByDate(LocalDate.now().toString())
    val importantTodos: Flow<List<Todo>> = dao.getImportantTodos()
    val scheduledTodos: Flow<List<Todo>> = dao.getScheduledTodos()
    val favoriteTodos: Flow<List<Todo>> = dao.getFavoriteTodos()
    val todosInBin: Flow<List<Todo>> = dao.getDeletedTodos()
    val finishedTodos: Flow<List<Todo>> = dao.getFinishedTodos()


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
        val todosList = allTableTodos.firstOrNull()
        todosList.isNullOrEmpty()
    }

    fun isAllEmpty(): Boolean = runBlocking{
        val todosList = allTodos.firstOrNull()
        todosList.isNullOrEmpty()
    }

    fun isTodayEmpty(): Boolean = runBlocking{
        val todosList = currentDateTodos.firstOrNull()
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