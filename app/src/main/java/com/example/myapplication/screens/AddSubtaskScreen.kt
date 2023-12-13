package com.example.myapplication.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.function.Notification.Notification
import com.example.myapplication.todoEntities.SubtaskTodo
import com.example.myapplication.todoViewModels.SubtaskTodoViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubtaskTodo(todoId: Long, onNavigate: () -> Unit) {
    val subtaskTodoViewModel: SubtaskTodoViewModel = viewModel()
    var title by remember { mutableStateOf("") }

    val context = LocalContext.current
    val buttonCoroutineScope = rememberCoroutineScope()

    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val timePickerState = rememberTimePickerState()
    var showDialogForDate by rememberSaveable { mutableStateOf(false) }
    var showDialogForTime by rememberSaveable { mutableStateOf(false) }
    val snackScope = rememberCoroutineScope()
    val timeFormatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val snackState = remember{ SnackbarHostState() }

    var dateStoreInDatabase by remember { mutableStateOf("null") }
    var timeStoreInDatabase by remember { mutableStateOf("null") }
    var isFinished by remember { mutableStateOf(false) }
    var isDeleted by remember { mutableStateOf(false) }

    var scheduledDate by remember { mutableStateOf<LocalDate?>(null) }
    var scheduledTime by remember { mutableStateOf<LocalTime?>(null) }

//    if (showDialogForDate) {
//        DatePickerDialog(
//            context,
//            { _, year, month, dayOfMonth ->
//                scheduledDate = LocalDate.of(year, month + 1, dayOfMonth)
//                showDialogForDate = false
//            },
//            scheduledDate?.year ?: LocalDate.now().year,
//            scheduledDate?.monthValue?.minus(1) ?: LocalDate.now().monthValue - 1,
//            scheduledDate?.dayOfMonth ?: LocalDate.now().dayOfMonth
//        ).show()
//    }
//
//    if (showDialogForTime) {
//        TimePickerDialog(
//            context,
//            { _, hour, minute ->
//                scheduledTime = LocalTime.of(hour, minute)
//                showDialogForTime = false
//            },
//            scheduledTime?.hour ?: LocalTime.now().hour,
//            scheduledTime?.minute ?: LocalTime.now().minute,
//            true
//        ).show()
//    }


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
                label = { Text("Subtask Title") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )

            fun insertOnClick() {
                buttonCoroutineScope.launch {
                    subtaskTodoViewModel.addSubtaskTodo(
                        SubtaskTodo(
                            id = todoId,
                            subtaskTitle = title,
                            subtaskScheduledDate = dateStoreInDatabase,
                            subtaskScheduledTime = timeStoreInDatabase,
                            isSubtaskCompleted = isFinished
                        )
                    )
                }
            }
            fun convertTimeTo24HourFormat(time: String?): String {
                var modifiedTime = time
                if(time == "null"){
                    modifiedTime = "00:00 am"
                }
                val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                try {
                        Log.d("Subtask","$time")
                        val date: Date = inputFormat.parse(modifiedTime)
                        return outputFormat.format(date)
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Handle parsing exception
                    return ""
                }
                return "00:00"
            }
            // Add Todo Button
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        insertOnClick()
                        getToast(context, "Data added successfully")
                        if(dateStoreInDatabase!= "null")
                        Notification.SetNotification(dateStoreInDatabase+ " "+convertTimeTo24HourFormat(timeStoreInDatabase) ,context, "Notification for subtask $title")
                        onNavigate()
                    } else {
                        getToast(context, "Title cannot be empty")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Subtask")
            }
            Row {
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
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}