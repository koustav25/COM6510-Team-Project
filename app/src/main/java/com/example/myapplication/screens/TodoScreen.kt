package com.example.myapplication.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.TodoCategories
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.SubtaskTodo
import com.example.myapplication.todoEntities.Todo
import com.example.myapplication.todoViewModels.SubtaskTodoViewModel
import com.example.myapplication.todoViewModels.TodoViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

var toBeDeletedRows = hashSetOf<Long>()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoDetail(selectedCategory: TodoCategories?, onNavigate: (Long) -> Unit){
    val todoViewModel: TodoViewModel = viewModel()
    val subtaskTodoViewModel: SubtaskTodoViewModel = viewModel()
    val buttonCoroutineScope = rememberCoroutineScope()
    val dao = TodoDatabase.getDatabase(LocalContext.current).todoDao()
LazyColumn(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxSize())
         {item{
             if(todoViewModel.isEmpty()){
                 Text(text = ("No Todos"))
             }
             else{
                 if(selectedCategory?.todoCategories == "Today"){
                     if(subtaskTodoViewModel.isEmpty()){
                         Todos(todo = todoViewModel.currentDateTodos, subtaskTodo = subtaskTodoViewModel.allSubtasks, todoViewModel){
                                 todoId -> onNavigate(todoId)
                         }
                     }
                     else{
                         Todos(todo = todoViewModel.currentDateTodos, subtaskTodo = subtaskTodoViewModel.allSubtasks, todoViewModel){
                                 todoId -> onNavigate(todoId)
                         }
                     }
                     //This function needs to be moved to fun Todos to make it global for every todo
                     //Currently this is just visible for Today category todos
                     TodoDeleteIcon()
                     Log.i("cat", "Todayyyyyyyyyy")
                 }
                 if(selectedCategory?.todoCategories == "All"){
                     Todos(todo = todoViewModel.allTodos, subtaskTodo = subtaskTodoViewModel.allSubtasks, todoViewModel){
                             todoId -> onNavigate(todoId)
                     }
                     TodoDeleteIcon()
                     Log.i("cat", "ALLLLLLLLL")
                 }
                 if(selectedCategory?.todoCategories == "Scheduled"){
                     if(todoViewModel.isScheduledEmpty()){
                         Text(text = "No Scheduled todos")
                     }
                     else{
                         Todos(todo = todoViewModel.scheduledTodos, subtaskTodo = subtaskTodoViewModel.allSubtasks ,todoViewModel){
                                 todoId -> onNavigate(todoId)
                         }
                     }
                     TodoDeleteIcon()
                     Log.i("cat", "Scheduled")
                 }
                 if(selectedCategory?.todoCategories == "Important"){
                     if(todoViewModel.isImportantEmpty()){
                         Text(text = "No Important todos")
                     }
                     else{
                         Todos(todo = todoViewModel.importantTodos, subtaskTodo = subtaskTodoViewModel.allSubtasks, viewModel = todoViewModel){
                                 todoId -> onNavigate(todoId)
                         }
                     }
                     TodoDeleteIcon()
                     Log.i("cat", "Important")
                 }
                 if(selectedCategory?.todoCategories == "Finished"){
                     Text(text = "No Todos here in Finished")
                     Log.i("cat", "Finished")
                 }
                 if(selectedCategory?.todoCategories == "Bin"){
                     Todos(todo = todoViewModel.todosInBin, subtaskTodo = subtaskTodoViewModel.allSubtasks, viewModel = todoViewModel){
                             todoId -> onNavigate(todoId)
                     }
                     DeleteIcon()
                     Log.i("cat", "Bin")
                 }
             }
        }
     }
}

@Composable
fun TodoDeleteIcon(){
    val buttonCoroutineScope = rememberCoroutineScope()
    val dao = TodoDatabase.getDatabase(LocalContext.current).todoDao()
    fun clickToDelete(){
        toBeDeletedRows.iterator().forEach { element->
            buttonCoroutineScope.launch {

                dao.delete(
                    element
                )
            }
        }
    }
    IconButton(onClick = {
        clickToDelete()
    }) {
        Icon(Icons.Default.Delete, contentDescription ="Delete" )
    }
}

@Composable
fun DeleteIcon(){
    val buttonCoroutineScope = rememberCoroutineScope()
    val dao = TodoDatabase.getDatabase(LocalContext.current).todoDao()
    fun clickToDelete(){
        toBeDeletedRows.iterator().forEach { element->
            buttonCoroutineScope.launch {

                dao.deleteFromBin(
                    element
                )
            }
        }
    }
    IconButton(onClick = {
        clickToDelete()
    }) {
        Icon(Icons.Default.Delete, contentDescription ="Delete" )
    }
}


@Composable
fun Todos(todo: Flow<List<Todo>>, subtaskTodo: Flow<List<SubtaskTodo>>, viewModel: TodoViewModel, onNavigate: (todoId: Long) -> Unit){
    val todosState by todo.collectAsState(initial = emptyList())
    val subtaskTodoState by subtaskTodo.collectAsState(initial = emptyList())

    Column {
        todosState.forEach { todoItem ->
            var isExpanded by remember { mutableStateOf(false) }
            val isChecked = remember{ mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()
            val textDecoration = if(isChecked.value) TextDecoration.LineThrough else null
            var isFavClicked by remember { mutableStateOf(todoItem.isFavorite) }
            var isImportantClicked by remember { mutableStateOf(false) }
            val favIcon = if (isFavClicked){
                Icons.Filled.Favorite
            }else{
                Icons.Outlined.FavoriteBorder
            }
            val impIcon = if (isImportantClicked){
                Icons.Filled.Info
            }else{
                Icons.Outlined.Info
            }
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clickable(onClick = { isExpanded = !isExpanded }),
                shape = RoundedCornerShape(CornerSize(10.dp)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 10.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = isChecked.value, onCheckedChange = {
                            isChecked.value = it
                        })
                        if(isChecked.value){
                            toBeDeletedRows.add(todoItem.id)
                        }else{
                            if(toBeDeletedRows.contains(todoItem.id))
                                toBeDeletedRows.remove(todoItem.id)
                        }

                        Text(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                                    append(todoItem.title)
                                }
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isFavClicked) {
                            var isFavClicked by remember { mutableStateOf(todoItem.isFavorite) }
                            val favIcon = if (isFavClicked) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            }
                            Icon(
                                imageVector = favIcon,
                                contentDescription = "Already Favorite",
                                modifier = Modifier.clickable {
                                    isFavClicked = !isFavClicked
                                    coroutineScope.launch {
                                        viewModel.setFavourite(todoItem.id, isFavClicked)
                                    }
                                }
                            )
                        }else {
                            Icon(
                                imageVector = favIcon,
                                contentDescription = "Not Favorite",
                                modifier = Modifier.clickable {
                                    isFavClicked = !isFavClicked
                                }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(0.9f),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                                        append(todoItem.description)
                                    }
                                })
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(textDecoration = if (isChecked.value) TextDecoration.LineThrough else null)) {
                                        append(todoItem.date)
                                    }
                                }
                            )
                        }
                        if (todoItem.isImportant) {
                            var isImportantClicked by remember { mutableStateOf(todoItem.isImportant) }
                            val impIcon = if (isImportantClicked){
                                Icons.Filled.Info
                            }else{
                                Icons.Outlined.Info
                            }
                            Icon(
                                imageVector = impIcon,
                                contentDescription = "Already Favorite",
                                modifier = Modifier.clickable {
                                    isImportantClicked = !isImportantClicked
                                }
                            )
                        } else {
                            Icon(
                                imageVector = impIcon,
                                contentDescription = "Not Favorite",
                                modifier = Modifier.clickable {
                                    isImportantClicked = !isImportantClicked
                                }
                            )
                        }
                    }

                    //Expand todo
                    if(isExpanded){
                        Divider(modifier = Modifier.padding(vertical = 5.dp),
                            thickness = 1.dp,
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(textDecoration = if (isChecked.value) TextDecoration.LineThrough else null)) {
                                    if(todoItem.scheduledDate == "null"){
                                        append("Scheduled Date: Not scheduled")
                                    }
                                    else{
                                        append("Scheduled Date: "+todoItem.scheduledDate)
                                    }
                                }
                            })
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(textDecoration = if (isChecked.value) TextDecoration.LineThrough else null)) {
                                    if(todoItem.scheduledTime == "null"){
                                        append("Scheduled Time: Not scheduled")
                                    }
                                    else{
                                        append("Scheduled Time: "+todoItem.scheduledTime)
                                    }

                                }
                            })

                        //Subtasks
                        if (subtaskTodoState.isEmpty()) {
                            Text(text = "No subtasks")
                        } else {
                            subtaskTodoState.forEach { subtaskItem ->
                                val subtaskIsChecked = remember { mutableStateOf(false) }
                                val textDecoration = if (subtaskIsChecked.value){ TextDecoration.LineThrough } else if(isChecked.value) { TextDecoration.LineThrough } else null
                                Row(
                                    modifier = Modifier
                                        .padding(5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (subtaskItem.id == todoItem.id) {
                                        if(isChecked.value){
                                            Checkbox(checked = isChecked.value, onCheckedChange = {})
                                        }
                                        else{
                                            Checkbox(checked = subtaskIsChecked.value, onCheckedChange = {
                                                subtaskIsChecked.value = it
                                            })
                                        }


                                        Text(
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            text = buildAnnotatedString {
                                                withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                                                    append(subtaskItem.subtaskTitle)
                                                }
                                            },
                                        )
                                    }
                                    else{
                                        Text(text = "No subtasks")
                                    }
                                }
                            }
                        }

                        //Divider
                        Divider(modifier = Modifier.padding(vertical = 5.dp),
                            thickness = 1.dp,
                        )
                        Row(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            //Delete Todo Button
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Edit Todo"
                                )
                            }

                            //Add Subtask
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier.clickable(onClick = {
                                        onNavigate(todoItem.id)
                                        Log.i("id", "${onNavigate(todoItem.id)}")
                                    }),
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Subtask"
                                )
                                Text(text = "Add Subtask")
                            }

                            //Edit Todo Button
                            IconButton(onClick = { }) {
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Todo")
                            }
                        }
                    }

                }
            }
        }
    }

}