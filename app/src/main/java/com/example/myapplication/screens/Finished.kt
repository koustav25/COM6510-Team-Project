package com.example.myapplication.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.todoViewModels.SubtaskTodoViewModel
import com.example.myapplication.todoViewModels.TodoViewModel

@Composable
fun Finished(onNavigate: (Long) -> Unit){
    val todoViewModel: TodoViewModel = viewModel()
    val subtaskTodoViewModel: SubtaskTodoViewModel = viewModel()
    LazyColumn(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize())
    {
        item {
            if (todoViewModel.isFinishedEmpty()) {
                Text(text = "No Finished todos")
            } else {
                Todos(todo = todoViewModel.finishedTodos, subtaskTodo = subtaskTodoViewModel.allSubtasks, todoViewModel){
                        todoId -> onNavigate(todoId)
                }
            }
        }
    }
}