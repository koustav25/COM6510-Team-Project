package com.example.myapplication.screens
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.TemplateTodos
import com.example.myapplication.function.Camera
import com.example.myapplication.function.Gallery
//import com.example.myapplication.function.Location
import com.example.myapplication.function.Web
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.SubtaskTodo
import com.example.myapplication.todoEntities.Todo
import com.example.myapplication.todoViewModels.SubtaskTodoViewModel
import com.example.myapplication.todoViewModels.TodoViewModel
import com.example.myapplication.ui.theme.Priority
import com.example.myapplication.ui.theme.PriorityTodosData
//import com.example.myapplication.ui.theme.Priority
//import com.example.myapplication.ui.theme.PriorityTodosData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodo(templateTodos: List<TemplateTodos>, onNavigate: () -> Unit) {
    val todoViewModel: TodoViewModel = viewModel()
    val subtaskTodoViewModel: SubtaskTodoViewModel = viewModel()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isFav by remember{ mutableStateOf(false) }
    var isImp by remember{ mutableStateOf(false) }
    var priorityVal by remember{ mutableStateOf(Priority.STANDARD) }

    var subTitle by remember { mutableStateOf("") }

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
//    var scheduledDate by remember { mutableStateOf<LocalDate?>(null) }
//    var scheduledTime by remember { mutableStateOf<LocalTime?>(null) }

//    Button(onClick = { showDialogForDate = true }) {
//        Text("Select Date")
//    }
//
//    // Trigger to open TimePicker
//    Button(onClick = { showDialogForTime = true }) {
//        Text("Select Time")
//    }
//
//    // Custom DatePicker
//    CustomDatePicker(
//        showDialog = showDialogForDate,
//        onDateSelected = { date ->
//            scheduledDate = date
//            showDialogForDate = false
//        },
//        onDismissRequest = { showDialogForDate = false }
//    )
//
//    // Custom TimePicker
//    CustomTimePicker(
//        showDialog = showDialogForTime,
//        onTimeSelected = { time ->
//            scheduledTime = time
//            showDialogForTime = false
//        },
//        onDismissRequest = { showDialogForTime = false }
//    )


    var isFavClicked by remember { mutableStateOf(false) }
    var isImportantClicked by remember{ mutableStateOf(false) }
    val favIcon = if (isFavClicked){
        Icons.Filled.Favorite
    }else{
        Icons.Outlined.FavoriteBorder
    }
    val impIcon = if(isImportantClicked){
        Icons.Filled.Info
    }else{
        Icons.Outlined.Info
    }

    var dropDownExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(templateTodos[0]) }

    var dropDownPriorityExpanded by remember { mutableStateOf(false) }
    var selectedPriority by remember{ mutableStateOf(PriorityTodosData()[0]) }

    var imageURI by remember{ mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            //Templates
            Text(
                text = "Select from templates",
            )
            ExposedDropdownMenuBox(
                expanded = dropDownExpanded,
                onExpandedChange = { dropDownExpanded = !dropDownExpanded },
                modifier = Modifier.padding(2.dp)
            ) {
                TextField(value = selectedText.taskName,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = dropDownExpanded,
                    onDismissRequest = { dropDownExpanded = false }
                ) {templateTodos.forEach { templates ->
                    DropdownMenuItem(
                        text = { Text(templates.taskName) },
                        onClick = {
                            selectedText = templates
                            dropDownExpanded = false
                        }
                    )
                }
                }
            }

//            Priority
            Text(
                text="Select a priority"
           )
            ExposedDropdownMenuBox(
                expanded = dropDownPriorityExpanded,
                onExpandedChange = { dropDownPriorityExpanded=!dropDownPriorityExpanded},
                modifier = Modifier.padding(2.dp)
            ){
                TextField(value = selectedPriority.priorityName,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownPriorityExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = dropDownPriorityExpanded,
                    onDismissRequest = { dropDownPriorityExpanded = false }
                ) {
                    PriorityTodosData().forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.priorityName) },
                        onClick = {
                            selectedPriority = item
                            dropDownPriorityExpanded = false
                        }
                    )
                }
                }
            }

            LaunchedEffect(selectedPriority ){
                priorityVal = selectedPriority.priority
            }

            LaunchedEffect(selectedText){
                title = selectedText.todoTitle
                description = selectedText.todoDescription
            }

            //Editing the templates
            if(selectedText.taskName != "Select a task") {
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

                //Subtasks && Edit
                selectedText.subTaskDetails.forEach { subtask ->
                    var isSubtaskExpanded by remember { mutableStateOf(false) }
                    var subtaskTitle by remember { mutableStateOf(subtask.subtaskTitle) }
                    var isSubtaskDateExpanded by remember { mutableStateOf(false) }
                    var isSubtaskTimeExpanded by remember { mutableStateOf(false) }
                    var subtaskScheduledDate by remember { mutableStateOf("null") }
                    var subtaskScheduledTime by remember { mutableStateOf("null") }

                    val isSubtaskChecked = remember { mutableStateOf(false) }
                    val textDecoration = if (isSubtaskChecked.value) TextDecoration.LineThrough else null



                    LaunchedEffect(subtask){
                        subtaskTitle = subtask.subtaskTitle
                    }
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .clickable(onClick = { isSubtaskExpanded = !isSubtaskExpanded }),
                        shape = RoundedCornerShape(CornerSize(10.dp)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            if(isSubtaskExpanded){
                                Row(
                                    modifier = Modifier
                                        .padding(5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row {
                                            OutlinedTextField(
                                                value = subtaskTitle,
                                                onValueChange = { subtaskTitle = it },
                                                label = { Text("Subtask Title") },
                                                keyboardOptions = KeyboardOptions.Default.copy(
                                                    imeAction = ImeAction.Next
                                                ),
                                                modifier = Modifier
                                                    .padding(bottom = 8.dp)
                                            )
                                            IconButton(onClick = {
                                                isSubtaskExpanded = false
                                                subtask.subtaskTitle = subtaskTitle
                                                subTitle = subtask.subtaskTitle
                                                subtask.subtaskScheduledDate = subtaskScheduledDate
                                                subtask.subtaskScheduledTime = subtaskScheduledTime
                                            })
                                            {
                                                Icon(
                                                    imageVector = Icons.Filled.Check,
                                                    contentDescription = "Add"
                                                )
                                            }
                                        }
                                        Row {
                                            //Need to figure out if the date and time picker can be done using a function to reduce the code redundancy
                                            //Date Picker
                                            IconButton(onClick = {
                                                isSubtaskDateExpanded = !isSubtaskDateExpanded
                                            }) {
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
                                            IconButton(onClick = {
                                                isSubtaskTimeExpanded = !isSubtaskTimeExpanded
                                            }) {
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


                                        ///
                                        if (isSubtaskDateExpanded) {
                                            DatePickerDialog(
                                                onDismissRequest = { isSubtaskDateExpanded = false },
                                                confirmButton = {
                                                    TextButton(onClick = {
                                                        val selectedDateMillis = datePickerState.selectedDateMillis
                                                        if(selectedDateMillis!=null){
                                                            subtaskScheduledDate = handleSelectedDate(selectedDateMillis)
                                                        }
                                                        isSubtaskDateExpanded = false
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
                                                        onClick = { isSubtaskDateExpanded = false }
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
                                        if (isSubtaskTimeExpanded) {
                                            TimePickerDialog(
                                                onCancel = { isSubtaskTimeExpanded = false },
                                                onConfirm = {
                                                    val cal = Calendar.getInstance()
                                                    cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                                    cal.set(Calendar.MINUTE, timePickerState.minute)
                                                    cal.isLenient = false
                                                    subtaskScheduledTime = timeFormatter.format(cal.time)
                                                    snackScope.launch {
                                                        snackState.showSnackbar("Entered time: ${timeFormatter.format(cal.time)}")
                                                    }
                                                    isSubtaskTimeExpanded = false
                                                }) {
                                                TimePicker(state = timePickerState)
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                Checkbox(
                                    checked = isSubtaskChecked.value,
                                    onCheckedChange = {
                                        isSubtaskChecked.value = it
                                        subtask.isSubtaskCompleted = isSubtaskChecked.value
                                    }
                                )

                                Text(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                                            append(subtask.subtaskTitle)
                                            subTitle = subtask.subtaskTitle
                                        }
                                    },
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                //insert subtasks
                fun insertTemplateTaskTodos() {
                    buttonCoroutineScope.launch {
                        val insertedTodoId = todoViewModel.addTodo(
                            Todo(
                                title = title,
                                description = description,
                                date = LocalDate.now().toString(),
                                time = LocalTime.now().toString(),
                                isFavorite = isFav,
                                isImportant = isImp,
                                scheduledDate = dateStoreInDatabase,
                                scheduledTime = timeStoreInDatabase,
                                isFinished = isFinished,
                                isDeleted = isDeleted,
                                priority = priorityVal,
                                imageUri = imageURI
                            )
                        )
                        Log.i("ids", "Todo inserted successfully: ${insertedTodoId}")
                        for(subtask in selectedText.subTaskDetails) {
                            subtaskTodoViewModel.addSubtaskTodo(
                                SubtaskTodo(
                                    id = insertedTodoId,
                                    subtaskTitle = subtask.subtaskTitle,
                                    subtaskScheduledDate = subtask.subtaskScheduledDate,
                                    subtaskScheduledTime = subtask.subtaskScheduledTime,
                                    isSubtaskCompleted = subtask.isSubtaskCompleted
                                )
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            insertTemplateTaskTodos()
                            getToast(context, "Data added successfully")
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
                            Text(text = "Fav", fontSize = 8.sp)
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
                            Text(text = "Imp", fontSize = 8.sp)
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
            else {
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

                fun insertTodo() {
                    buttonCoroutineScope.launch {
                        todoViewModel.addTodo(
                            Todo(
                                title = title,
                                description = description,
                                date = LocalDate.now().toString(),
                                time = LocalTime.now().toString(),
                                isFavorite = isFav,
                                isImportant = isImp,
                                scheduledDate = dateStoreInDatabase,
                                scheduledTime = timeStoreInDatabase,
                                isFinished = isFinished,
                                isDeleted = isDeleted,
                                priority = priorityVal,
                                imageUri = imageURI
                            )
                        )
                    }
                }

                // Add Todo Button
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            insertTodo()
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
                            Text(text = "Fav", fontSize = 8.sp)
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
                            Text(text = "Imp", fontSize = 8.sp)
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
                                Icons.Outlined.DateRange, "Time Picker"
                            )
                            Text(text = "Time", fontSize = 8.sp)
                        }
                    }
                }
                Row{
                    //Open web
                    IconButton(onClick = { Web.OpenWeb(context) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .size(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Outlined.Search, "Web"
                            )
                            Text(text = "Web", fontSize = 8.sp)
                        }
                    }
                    //location
//                    Location.GetLocation(context)
                    //open Gallery

                    Gallery.OpenGallery(context){
                            selectedUri ->
                        imageURI = selectedUri
                    }
                    //open Camera
                    Camera.RunCamera(context){
                        selectedUri ->
                        imageURI = selectedUri
                    }
                }
                //Handling Date dialog
                if (showDialogForDate) {
                    DatePickerDialog(
                        onDismissRequest = { showDialogForDate = false },
                        confirmButton = {
                            TextButton(onClick = {
                                val selectedDateMillis = datePickerState.selectedDateMillis
                                if (selectedDateMillis != null) {
                                    dateStoreInDatabase = handleSelectedDate(selectedDateMillis)
                                }
                                showDialogForDate = false
                                snackScope.launch {
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