package com.example.myapplication.todoViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.SubtaskTodo
import com.example.myapplication.todoEntities.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import android.util.Log


class SubtaskTodoViewModel(app: Application) : AndroidViewModel(app) {
    private val context = getApplication<Application>().applicationContext
    private val subtaskDao = TodoDatabase.getDatabase(context).subtaskDao()

    val allSubtasks: Flow<List<SubtaskTodo>> = subtaskDao.getAllSubtasks()

    fun isEmpty(): Boolean = runBlocking{
        val todosList = allSubtasks.firstOrNull()
        todosList.isNullOrEmpty()
    }

    fun addSubtaskTodo(subTaskTodo: SubtaskTodo) {
        viewModelScope.launch(Dispatchers.IO) {
            subtaskDao.insertSubtask(subTaskTodo)
        }
    }

    fun getSubtasksById(todoId: Long){
        viewModelScope.launch(Dispatchers.IO){
            subtaskDao.getSubtasksById(todoId)
        }
    }

    fun deleteSubtaskTodo(subTaskListTodo: List<SubtaskTodo>){
        viewModelScope.launch(Dispatchers.IO){
            subTaskListTodo.forEach{
                subtaskDao.delete(it)
            }

        }
    }

    fun updateSubtaskTodo(subTaskTodo: SubtaskTodo){
        viewModelScope.launch(Dispatchers.IO){
            subtaskDao.update(subTaskTodo)
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