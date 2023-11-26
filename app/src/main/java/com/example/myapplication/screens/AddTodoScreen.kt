package com.example.myapplication.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.Todo
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodo(onNavigate: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current
    val buttonCoroutineScope = rememberCoroutineScope()
    val dao = TodoDatabase.getDatabase(LocalContext.current).todoDao()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Todo Title TextField
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Todo Title") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )

        // Todo Description TextField
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Todo Description") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )

        fun insertOnClick(){
            buttonCoroutineScope.launch {
                dao.insert(
                    Todo(
                        title = title,
                        description = description,
                        date = LocalDate.now().toString()
                    )
                )
            }
        }

        // Add Todo Button
        Button(
            onClick = {
                if(title.isNotBlank()){
                    insertOnClick()
                    getToast(context, "Todo added!")
                    onNavigate()
                }
                else{
                    getToast(context, "Title cannot be empty")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Todo")
        }
    }
}

fun getToast(context: Context, msg: String){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}