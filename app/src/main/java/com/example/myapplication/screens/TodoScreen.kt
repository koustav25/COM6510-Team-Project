package com.example.myapplication.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.todoEntities.Todo
import com.example.myapplication.todoViewModels.TodoViewModel
import kotlinx.coroutines.flow.Flow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoDetail(onNavigate: () -> Unit){
    val todoViewModel: TodoViewModel = viewModel()
LazyColumn(
        modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize())
         {item{
             Todos(todo = todoViewModel.allTodos, todoViewModel)
             Button(onClick = { onNavigate() } ) {
             Text(text = "Go to Home Screen")
            }
        }
     }
}

@Composable
fun Todos(todo: Flow<List<Todo>>, viewModel: TodoViewModel){
    val todosState by todo.collectAsState(initial = emptyList())
    val isChecked = remember{ mutableStateOf(false) }
    val textDecoration = if(isChecked.value) TextDecoration.LineThrough else null
    Column {
        todosState.forEach { todoItem ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clickable(onClick = { }),
                shape = RoundedCornerShape(CornerSize(10.dp)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val isChecked = remember{ mutableStateOf(false) }
                    val textDecoration = if(isChecked.value) TextDecoration.LineThrough else null
                    Checkbox(checked = isChecked.value, onCheckedChange = {
                        isChecked.value = it
                    })

                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                            append(todoItem.title)
                        }
                    })
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                            append(todoItem.description)
                        }
                    })
            }
        }
    }

}