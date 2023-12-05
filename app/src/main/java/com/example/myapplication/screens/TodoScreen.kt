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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
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
                     if(todoViewModel.isFinishedEmpty()){
                         Text(text = "No Finished Todos here")
                     }
                     Todos(todo = todoViewModel.finishedTodos, subtaskTodo = subtaskTodoViewModel.allSubtasks, viewModel = todoViewModel){
                             todoId -> onNavigate(todoId)
                     }

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
fun Todos(todo: Flow<List<Todo>>, subtaskTodo: Flow<List<SubtaskTodo>>, viewModel: TodoViewModel, onNavigate: (todoId: Long) -> Unit) {
    val todosState by todo.collectAsState(initial = emptyList())
    val subtaskTodoState by subtaskTodo.collectAsState(initial = emptyList())

    Column {
        todosState.forEach { todoItem ->
            var isExpanded by remember { mutableStateOf(false) }
//            val isChecked = remember { mutableStateOf(false) }
            val isChecked = remember { mutableStateOf(todoItem.isFinished) }
            val coroutineScope = rememberCoroutineScope()
            val textDecoration = if (isChecked.value) TextDecoration.LineThrough else null
            var isFavClicked by remember { mutableStateOf(todoItem.isFavorite) }
            var isImportantClicked by remember { mutableStateOf(todoItem.isImportant) }
            var editingTitle by remember { mutableStateOf(todoItem.title) }
            var subtask by remember { mutableStateOf(false) }
            var editingDescription by remember { mutableStateOf(todoItem.description) }
            var isEditing  by remember { mutableStateOf(false) }
            val favIcon = if (isFavClicked) {
                Icons.Filled.Favorite
            } else {
                Icons.Outlined.FavoriteBorder
            }
            val impIcon = if (isImportantClicked) {
                Icons.Filled.Info
            } else {
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

                        if (isChecked.value) {
                            toBeDeletedRows.add(todoItem.id)
                            Log.d("AA", " "+isChecked.value)
//                            coroutineScope.launch {
//                                viewModel.setFinished(todoItem.id, isChecked.value)
//                            }
                        } else {
//                            Log.d("TAG", " "+todoItem.id + " ")
                            if (toBeDeletedRows.contains(todoItem.id))
                                toBeDeletedRows.remove(todoItem.id)

                        }
                        if(todoItem.isFinished != isChecked.value) {
//                            Log.d("Debug", " Entered the if loop "+isChecked.value)
                            viewModel.setFinished(todoItem.id, isChecked.value)
                        }

                        if(isEditing){
                            OutlinedTextField(
                                value = editingTitle,
                                onValueChange = {
                                    editingTitle = it
                                },
                                label = { Text("Title") },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                            )

                        }else{
                        Text(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                                    append(todoItem.title)
                                }
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )}


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
                        } else {
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
                            if(isEditing){
                                OutlinedTextField(
                                    value = editingDescription,
                                    onValueChange = { editingDescription = it },
                                    label = { Text("Description") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }else{
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                                        append(todoItem.description)
                                    }
                                })}
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(textDecoration = if (isChecked.value) TextDecoration.LineThrough else null)) {
                                        append(todoItem.date)
                                    }
                                }
                            )
                        }
                        if (isImportantClicked) {
                            var isImportantClicked by remember { mutableStateOf(todoItem.isImportant) }
                            val impIcon = if (isImportantClicked) {
                                Icons.Filled.Info
                            } else {
                                Icons.Outlined.Info
                            }
                            Icon(
                                imageVector = impIcon,
                                contentDescription = "Already Favorite",
                                modifier = Modifier.clickable {
                                    isImportantClicked = !isImportantClicked
                                    coroutineScope.launch {
                                        viewModel.setImportant(todoItem.id, isImportantClicked)
                                    }
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
                    if (isExpanded) {
                        Divider(
                            modifier = Modifier.padding(vertical = 5.dp),
                            thickness = 1.dp,
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(textDecoration = if (isChecked.value) TextDecoration.LineThrough else null)) {
                                    if (todoItem.scheduledDate == "null") {
                                        append("Scheduled Date: Not scheduled")
                                    } else {
                                        append("Scheduled Date: " + todoItem.scheduledDate)
                                    }
                                }
                            })
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(textDecoration = if (isChecked.value) TextDecoration.LineThrough else null)) {
                                    if (todoItem.scheduledTime == "null") {
                                        append("Scheduled Time: Not scheduled")
                                    } else {
                                        append("Scheduled Time: " + todoItem.scheduledTime)
                                    }

                                }
                            })
                        val subtaskTodoViewModel: SubtaskTodoViewModel = viewModel()
                        var subtasksToBeDeleted by remember {mutableStateOf < List<SubtaskTodo>>(emptyList())}
                        //Subtasks
                        if (subtaskTodoState.isEmpty()) {
                            Text(text = "No subtasks")
                        } else {


                            subtaskTodoState.filter { it.id == todoItem.id }
                                .forEach { subtaskItem ->
                                    var subtaskEditing by remember { mutableStateOf(subtaskItem.subtaskTitle) }
                                    var isSubtaskChecked by remember { mutableStateOf(subtaskItem.isSubtaskCompleted) }
                                    val textDecoration = if (isSubtaskChecked) {
                                        TextDecoration.LineThrough
                                    } else if (isChecked.value) {
                                        TextDecoration.LineThrough
                                    } else {
                                        null
                                    }
                                    //Row
                                    Row(
                                        modifier = Modifier
                                            .padding(5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (isSubtaskChecked) {
                                            Checkbox(
                                                checked = isSubtaskChecked,
                                                onCheckedChange = { isSubtaskChecked = !isSubtaskChecked })
                                                if(isSubtaskChecked){
                                                    subtasksToBeDeleted = subtasksToBeDeleted + subtaskItem
                                                }else{
                                                    subtasksToBeDeleted = subtasksToBeDeleted - subtaskItem
                                                }
                                        } else {
                                            Checkbox(
                                                checked = isSubtaskChecked,
                                                onCheckedChange = {
                                                    isSubtaskChecked = it
                                                })
                                        }
                                        if(isEditing){
                                            OutlinedTextField(
                                                value = subtaskEditing,
                                                onValueChange = { subtaskEditing = it },
                                                label = { Text("Title") },
                                                keyboardOptions = KeyboardOptions.Default.copy(
                                                    imeAction = ImeAction.Done
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        }else{
                                        Text(
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            text = buildAnnotatedString {
                                                withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                                                    append(subtaskItem.subtaskTitle)
                                                }
                                            },
                                        )}
                                    }

                                    if(subtask){

                                        subtaskTodoViewModel.updateSubtaskTodo(
                                        subtaskItem.copy(
                                            subtaskTitle = subtaskEditing,
                                         )
                                        )
                                    }
                                }
                        }

                        //Divider
                        Divider(
                            modifier = Modifier.padding(vertical = 5.dp),
                            thickness = 1.dp,
                        )
                        Row(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            //Delete Todo Button
                            IconButton(onClick = {
                                subtaskTodoViewModel.deleteSubtaskTodo(subtasksToBeDeleted)

                            }) {
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
                            if(isEditing){
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Checked",
                                    modifier = Modifier.clickable {
                                        viewModel.updateTodo(
                                            todoItem.copy(
                                                title = editingTitle,
                                                description = editingDescription,
                                            )
                                        )
                                        subtask = true
                                        isEditing = false
                                    }
                                )
                            }else {
                                //Edit Todo Button
                                IconButton(onClick = {
                                    isEditing = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Edit Todo"
                                    )
                                }
                            }

                        }

                    }
                }
            }
        }

    }
}


