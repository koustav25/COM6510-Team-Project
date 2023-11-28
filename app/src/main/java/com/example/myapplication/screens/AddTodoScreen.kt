package com.example.myapplication.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.Todo
import com.example.myapplication.todoViewModels.TodoViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodo(onNavigate: () -> Unit) {
    val todoViewModel: TodoViewModel = viewModel()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isFav by remember{ mutableStateOf(false) }
    var isImp by remember{ mutableStateOf(false) }

    val context = LocalContext.current
    val buttonCoroutineScope = rememberCoroutineScope()

    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val timePickerState = rememberTimePickerState()
    var showDialogForDate by rememberSaveable { mutableStateOf(false) }
    var showDialogForTime by rememberSaveable { mutableStateOf(false) }
    val snackScope = rememberCoroutineScope()
    val timeFormatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val snackState = remember{ SnackbarHostState() }
    val configuration = LocalConfiguration.current
    var dateStoreInDatabase by remember { mutableStateOf("null") }
    var timeStoreInDatabase by remember { mutableStateOf("null") }
    var isFinished by remember { mutableStateOf(false) }
    var isDeleted by remember { mutableStateOf(false) }



    var isFavClicked by remember { mutableStateOf(false) }
    var isImportantClicked by remember{ mutableStateOf(false) }
    val favIcon = if (isFavClicked){
        Icons.Filled.Favorite
    }else{
        Icons.Outlined.FavoriteBorder
    }
    val impIcon = if(isImportantClicked){
        Icons.Filled.CheckCircle
    }else{
        Icons.Outlined.CheckCircle
    }
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
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

            fun insertOnClick() {
                buttonCoroutineScope.launch {
                    todoViewModel.addTodo(
                        Todo(
                            title = title,
                            description = description,
                            date = LocalDate.now().toString(),
                            isFavorite = isFav,
                            isImportant = isImp,
                            scheduledDate = dateStoreInDatabase,
                            scheduledTime = timeStoreInDatabase,
                            isFinished = isFinished,
                            isDeleted = isDeleted
                        )
                    )
                }
            }

            // Add Todo Button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        insertOnClick()
                        getToast(context, "Todo added!")
                        onNavigate()
                    } else {
                        getToast(context, "Title cannot be empty")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Todo")
            }
            Row {
                IconButton(
                    onClick = {
                        isFavClicked = !isFavClicked
                        isFav = !isFav
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .size(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Icon(favIcon, contentDescription = "Favorite")
                        Text(text = "Favorite", fontSize = 8.sp)
                    }
                }
                IconButton(
                    onClick = {
                        isImportantClicked = !isImportantClicked
                        isImp = !isImp
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .size(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Icon(impIcon, contentDescription = "Important")
                        Text(text = "Important", fontSize = 8.sp)
                    }
                }
                //Date Picker
                IconButton(onClick = { showDialogForDate = !showDialogForDate }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .size(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.DateRange, "Date Picker"
                        )
                        Text(text = "Date", fontSize = 8.sp)
                    }
                }

                //Time Picker
                IconButton(onClick = { showDialogForTime = !showDialogForTime }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .size(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Outlined.Info, "Time Picker"
                        )
                        Text(text = "Time", fontSize = 8.sp)
                    }
                }
            }

            //Handling Date dialog
            if (showDialogForDate) {
                DatePickerDialog(
                    onDismissRequest = { showDialogForDate = false },
                    confirmButton = {
                        TextButton(onClick = {
                            val selectedDateMillis = datePickerState.selectedDateMillis
                            if(selectedDateMillis!=null){
                                dateStoreInDatabase = handleSelectedDate(selectedDateMillis)
                            }
                            showDialogForDate = false
                            snackScope.launch{
                                snackState.showSnackbar(
                                    "Selected Date: ${datePickerState.selectedDateMillis}"
                                )
                            }
                        }
                        ) {
                            Text(text = "Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDialogForDate = false }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            //Handling Time Dialog
            if (showDialogForTime) {
                TimePickerDialog(
                    onCancel = { showDialogForTime = false },
                    onConfirm = {
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        cal.set(Calendar.MINUTE, timePickerState.minute)
                        cal.isLenient = false
                        timeStoreInDatabase = timeFormatter.format(cal.time)
                        snackScope.launch {
                            snackState.showSnackbar("Entered time: ${timeFormatter.format(cal.time)}")
                        }
                        showDialogForTime = false
                    }) {
                    TimePicker(state = timePickerState)
                }
            }

            //Horizontal Divider line
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
        }
    }

//Other Functions
fun getToast(context: Context, msg: String){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun handleSelectedDate(selectedDateMillis: Long): String{
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    val formattedDate = dateFormatter.format(Date(selectedDateMillis))
    return formattedDate.toString()
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = onConfirm
                    ) { Text("OK") }
                }
            }
        }
    }
}