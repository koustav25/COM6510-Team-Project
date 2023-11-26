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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.TodoCategories
import com.example.myapplication.todoEntities.Todo
import com.example.myapplication.todoViewModels.TodoViewModel
import kotlinx.coroutines.flow.Flow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoDetail(selectedCategory: TodoCategories?, onNavigate: () -> Unit){
    val todoViewModel: TodoViewModel = viewModel()
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
                     Todos(todo = todoViewModel.currentDateTodos, todoViewModel)
                     Button(onClick = { onNavigate() }) {
                         Text(text = "Go to Home Screen")
                     }
                     Log.i("cat", "Todayyyyyyyyyy")
                 }
                 if(selectedCategory?.todoCategories == "All"){
                     Todos(todo = todoViewModel.allTodos, todoViewModel)
                     Button(onClick = { onNavigate() } ) {
                         Text(text = "Go to Home Screen")
                     }
                     Log.i("cat", "ALLLLLLLLL")
                 }
                 if(selectedCategory?.todoCategories == "Scheduled"){
                     Text(text = "No Todos here in Scheduled") //Placeholder for now
                     Button(onClick = { onNavigate() }) {
                         Text(text = "Go to Home Screen")
                     }
                     Log.i("cat", "Scheduled")
                 }
                 if(selectedCategory?.todoCategories == "Important"){
                     Todos(todo = todoViewModel.importantTodos, viewModel = todoViewModel)
                     Button(onClick = { onNavigate() }) {
                         Text(text = "Go to Home Screen")
                     }
                     Log.i("cat", "Important")
                 }
                 if(selectedCategory?.todoCategories == "Finished"){
                     Text(text = "No Todos here in Finished")  //Placeholder for now
                     Button(onClick = { onNavigate() }) {
                         Text(text = "Go to Home Screen")
                     }
                     Log.i("cat", "Finished")
                 }
                 if(selectedCategory?.todoCategories == "Bin"){
                     Text(text = "No Todos here")  //Placeholder for now
                     Button(onClick = { onNavigate() }) {
                         Text(text = "Go to Home Screen")
                     }
                     Log.i("cat", "Bin")
                 }
             }
        }
     }
}

@Composable
fun Todos(todo: Flow<List<Todo>>, viewModel: TodoViewModel){
    val todosState by todo.collectAsState(initial = emptyList())
    Column {
        todosState.forEach { todoItem ->
            val isChecked = remember{ mutableStateOf(false) }
            val textDecoration = if(isChecked.value) TextDecoration.LineThrough else null
            var isFavClicked by remember { mutableStateOf(false) }
            var isImportantClicked by remember { mutableStateOf(false) }
            val favIcon = if (isFavClicked){
                Icons.Filled.Favorite
            }else{
                Icons.Outlined.FavoriteBorder
            }
            val impIcon = if (isImportantClicked){
                Icons.Filled.CheckCircle
            }else{
                Icons.Outlined.CheckCircle
            }
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clickable(onClick = { }),
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
                        if (todoItem.isFavorite) {
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
                                Icons.Filled.CheckCircle
                            }else{
                                Icons.Outlined.CheckCircle
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
                }
            }
        }
    }

}