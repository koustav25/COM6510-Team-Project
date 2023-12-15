package com.example.myapplication.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.TodoCategories
import com.example.myapplication.function.Location.Location.Resume
import com.example.myapplication.function.Notification.Notification
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.SubtaskTodo
import com.example.myapplication.todoEntities.Todo
import com.example.myapplication.todoViewModels.SubtaskTodoViewModel
import com.example.myapplication.todoViewModels.TodoViewModel
import com.example.myapplication.ui.theme.Priority
import com.example.myapplication.ui.theme.PriorityTodosData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale

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
    {item {

        if(todoViewModel.isEmpty()){
            Text("No todos. Please add some todos.")
        }
        else {
            if (selectedCategory?.todoCategories == "Today") {
                if (todoViewModel.isTodayEmpty()) {
                    Text(text = ("No todos created today"))
                }
                else {
                    Todos(
                        todo = todoViewModel.currentDateTodos,
                        subtaskTodo = subtaskTodoViewModel.allSubtasks,
                        0,
                        todoViewModel
                    ) { todoId ->
                        onNavigate(todoId)
                    }
                    TodoDeleteIcon()
                }
            }


            if (selectedCategory?.todoCategories == "All") {
                if (todoViewModel.isAllEmpty()) {
                    Text(text = ("No Todos"))
                }
                else {
                    Todos(
                        todo = todoViewModel.allTodos,
                        subtaskTodo = subtaskTodoViewModel.allSubtasks,
                        0,
                        todoViewModel
                    ) { todoId ->
                        onNavigate(todoId)
                    }
                    TodoDeleteIcon()
                }
            }


            if (selectedCategory?.todoCategories == "Scheduled") {
                if (todoViewModel.isScheduledEmpty()) {
                    Text(text = ("No scheduled todos"))
                }
                else {
                    Todos(
                        todo = todoViewModel.scheduledTodos,
                        subtaskTodo = subtaskTodoViewModel.allSubtasks,
                        0,
                        todoViewModel
                    ) { todoId ->
                        onNavigate(todoId)
                    }
                    TodoDeleteIcon()
                }
            }

            if (selectedCategory?.todoCategories == "Important") {
                if (todoViewModel.isImportantEmpty()) {
                    Text(text = "No Important todos")
                } else {
                    Todos(
                        todo = todoViewModel.importantTodos,
                        subtaskTodo = subtaskTodoViewModel.allSubtasks,
                        0,
                        viewModel = todoViewModel
                    ) { todoId ->
                        onNavigate(todoId)
                    }
                    TodoDeleteIcon()
                }
            }


            if (selectedCategory?.todoCategories == "Finished") {
                if (todoViewModel.isFinishedEmpty()) {
                    Text(text = "No Finished Todos")
                }
                else {
                    Todos(
                        todo = todoViewModel.finishedTodos,
                        subtaskTodo = subtaskTodoViewModel.allSubtasks,
                        4,
                        viewModel = todoViewModel
                    ) { todoId ->
                        onNavigate(todoId)
                    }
                }
            }


            if (selectedCategory?.todoCategories == "Bin") {
                if (todoViewModel.isBinEmpty()) {
                    Text(text = "No todos in the bin")
                }
                else {
                    Log.d("select", "$todoViewModel.todosInBin")
                    Todos(
                        todo = todoViewModel.todosInBin,
                        subtaskTodo = subtaskTodoViewModel.allSubtasks,
                        5,
                        viewModel = todoViewModel
                    ) { todoId -> onNavigate(todoId) }
                }
            }
        }
    }
    }
}


@Composable
fun TodoDeleteIcon(){
    val todoViewModel: TodoViewModel = viewModel()
    fun clickToDelete(){
        toBeDeletedRows.iterator().forEach { element->

            todoViewModel.sentToBin(element)
        }
    }
    IconButton(onClick = {
        clickToDelete()
    }) {
        Icon(Icons.Default.Delete, contentDescription ="Delete" )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteIcon(){
    val context = LocalContext.current
    val buttonCoroutineScope = rememberCoroutineScope()
    val dao = TodoDatabase.getDatabase(LocalContext.current).todoDao()

    var isConfirmationDialogVisible by remember { mutableStateOf(false) }

    fun showConfirmationDialog(){
        isConfirmationDialogVisible = true
    }

    fun hideConfirmationDialog(){
        isConfirmationDialogVisible = false
    }

    fun clickToDelete(){
        if(isConfirmationDialogVisible) {
            buttonCoroutineScope.launch {
                toBeDeletedRows.iterator().forEach { element ->
                    dao.deleteFromBin(
                        element
                    )
                }
                hideConfirmationDialog()
            }
        }else{
            showConfirmationDialog()
        }
    }
    Column {
        IconButton(onClick = {
            clickToDelete()
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
        if(isConfirmationDialogVisible){
            AlertDialog(
                onDismissRequest = {
                    hideConfirmationDialog()
                }
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(CornerSize(10.dp)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Confirmation",
                            textAlign = TextAlign.Center)
                        Divider(
                            modifier = Modifier.padding(vertical = 5.dp),
                            thickness = 1.dp,
                        )
                        Text(text = "Are you sure you want to delete the todo?",textAlign = TextAlign.Center)
                        Divider(
                            modifier = Modifier.padding(vertical = 5.dp),
                            thickness = 1.dp,
                        )
                        Row {
                            Button(onClick = { hideConfirmationDialog() }) {
                                Text(text = "Cancel")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(onClick = { clickToDelete() }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }

    }
}


//Comparator function for sorting todos by priority in ascending order
fun priorityComparatorASC() = Comparator<Todo>{ a, b ->
    when {
        (a.priority == b.priority) -> 0
        (a.priority == Priority.HIGH && b.priority == Priority.STANDARD) -> -1
        (a.priority == Priority.HIGH && b.priority == Priority.LOW) -> -1
        (a.priority == Priority.STANDARD && b.priority == Priority.LOW) -> -1
        else -> 0
    }
}

//Comparator function for sorting todos by priority in descending order
fun priorityComparatorDSC() = Comparator<Todo>{ a, b ->
    when {
        (a.priority == b.priority) -> 0
        (a.priority == Priority.STANDARD && b.priority == Priority.HIGH) -> -1
        (a.priority == Priority.LOW && b.priority == Priority.HIGH) -> -1
        (a.priority == Priority.LOW && b.priority == Priority.STANDARD) -> -1
        else -> 0
    }
}

//Comparator function for sorting todos by date in ascending order
@SuppressLint("SimpleDateFormat")
fun dateComparatorASC() = Comparator<Todo>{ a, b ->
    val format = SimpleDateFormat("yyyy-MM-dd")
    val date1: Date
    val date2: Date
    if(a.scheduledDate.equals("null")){
        Log.d("mine","a is null")
        date1 = format.parse("0000-00-00")!!
    }else{
        date1 = format.parse(a.scheduledDate)!!
    }
    if(b.scheduledDate.equals("null")){
        Log.d("mine","b is null")
        date2 = format.parse("0000-00-00")!!
    }else{
        date2 = format.parse(b.scheduledDate)!!
    }
    when {
        (date1 < date2) -> -1
        (date1 == date2) -> 0
        else -> 0
    }
}

//Comparator function for sorting todos by date in descending order
@SuppressLint("SimpleDateFormat")
fun dateComparatorDSC() = Comparator<Todo>{ a, b ->
    val format = SimpleDateFormat("yyyy-MM-dd")
    val date1: Date
    val date2: Date
    if(a.scheduledDate.equals("null")){
        Log.d("mine","a is null")
        date1 = format.parse("0000-00-00")!!
    }else{
        date1 = format.parse(a.scheduledDate)!!
    }
    if(b.scheduledDate.equals("null")){
        Log.d("mine","b is null")
        date2 = format.parse("0000-00-00")!!
    }else{
        date2 = format.parse(b.scheduledDate)!!
    }
    when {
        (date1 > date2) -> -1
        (date1 == date2) -> 0
        else -> 0
    }
}

//Comparator function for sorting todos by geolocation in ascending order by distance
fun geoLocationComparatorASC() = Comparator<Todo>{ a, b ->
    var alat = a.latitude
    if(alat.isNullOrEmpty()){
        alat = 0.toString();
    }
    var alon = a.longitude
    if(alon.isNullOrEmpty()){
        alon = 0.toString();
    }
    var blat = b.latitude
    if(blat.isNullOrEmpty()){
        blat = 0.toString();
    }
    var blon = b.longitude
    if(blon.isNullOrEmpty()){
        blon = 0.toString();
    }

    when {
        (alat < blat && alon < blon) -> -1
        (blat < alat && blon < alon) -> -1
        else -> 0
    }
}

fun getTodoScreenToast(context: Context, msg: String){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Todos(todo: Flow<List<Todo>>, subtaskTodo: Flow<List<SubtaskTodo>>, screenId: Int, viewModel: TodoViewModel, onNavigate: (todoId: Long) -> Unit) {
    val todosState by todo.collectAsState(initial = emptyList())
    val subtaskTodoState by subtaskTodo.collectAsState(initial = emptyList())
    val context = LocalContext.current
    var comparator by remember { mutableStateOf(priorityComparatorASC()) }
    Collections.sort(todosState, comparator);
    Column {
        Row(horizontalArrangement = Arrangement.Start
        ) {
            var isDateCompTrue by remember{ mutableStateOf(false) }
            var isPriorityCompTrue by remember{ mutableStateOf(false)}
            var isGeolocCompTrue by remember{ mutableStateOf(false)}
            Button(onClick = {
                isDateCompTrue=!isDateCompTrue;
                if(isDateCompTrue){
                    comparator = dateComparatorASC();
                    getTodoScreenToast(context, "Todos Sorted By Date ASC")
                }else{
                    comparator = dateComparatorDSC();
                    getTodoScreenToast(context, "Todos Sorted By Date DSC")
                }

            }) {
                if(isDateCompTrue)Text(text = "Date By ASC")
                else Text(text = "Date By DSC")
            }

            Button(onClick = {
                isPriorityCompTrue=!isPriorityCompTrue
                if(isPriorityCompTrue){
                    comparator = priorityComparatorASC();
                    getTodoScreenToast(context, "Todos Sorted By Priority ASC")
                }else{
                    comparator = priorityComparatorDSC();
                    getTodoScreenToast(context, "Todos Sorted By Priority DSC")
                }
            }) {
                if(isPriorityCompTrue)Text(text = "Priority By ASC")
                else Text(text = "Priority By DSC")
            }

            Button(onClick = {
                isGeolocCompTrue=!isGeolocCompTrue
                if(isGeolocCompTrue){
                    comparator = geoLocationComparatorASC()
                    getTodoScreenToast(context, "Todos Sorted By Location ASC")
                }else{
                    comparator = geoLocationComparatorASC()
                    getTodoScreenToast(context, "Todos Sorted By Location DSC")
                }
            }) {
                if(isGeolocCompTrue)Text(text = "Location By ASC")
                else Text(text = "Location By DSC")
            }
        }

        todosState.forEach { todoItem ->
            var isExpanded by remember { mutableStateOf(false) }
            val isChecked = remember { mutableStateOf(todoItem.isFinished) }
            val coroutineScope = rememberCoroutineScope()
            val textDecoration = if (isChecked.value) TextDecoration.LineThrough else null
            var isFavClicked by remember { mutableStateOf(todoItem.isFavorite) }
            var isImportantClicked by remember { mutableStateOf(todoItem.isImportant) }
            var editingTitle by remember { mutableStateOf(todoItem.title) }
            var subtask by remember { mutableStateOf(false) }
            var editingDescription by remember { mutableStateOf(todoItem.description) }
            var isEditing  by remember { mutableStateOf(false) }
            val context = LocalContext.current

            var isEditingDate by remember { mutableStateOf(false) }
            var isEditingTime by remember { mutableStateOf(false) }
            val editDatePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
            val editTimePickerState = rememberTimePickerState()
            val snackScope = rememberCoroutineScope()
            val editTimeFormatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
            val snackState = remember{ SnackbarHostState() }
            var editedScheduledDate by remember { mutableStateOf(todoItem.scheduledDate) }
            var editedScheduledTime by remember { mutableStateOf(todoItem.scheduledTime) }

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
            var dropDownPriorityExpanded by remember { mutableStateOf(false) }
            var selectedPriority by remember{ mutableStateOf(todoItem.priority) }

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
                        if (screenId != 4) {
                            Checkbox(checked = isChecked.value, onCheckedChange = {
                                isChecked.value = it
                            })
                        }
                        if (isChecked.value) {
                            toBeDeletedRows.add(todoItem.id)
                        } else {
                            if (toBeDeletedRows.contains(todoItem.id))
                                toBeDeletedRows.remove(todoItem.id)

                        }
                        if (todoItem.isFinished != isChecked.value) {
                            viewModel.setFinished(todoItem.id, isChecked.value)
                        }


                        if (isEditing) {
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

                        } else {
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
                        }


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
                            if (isEditing) {
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
                            } else {
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                                            append(todoItem.description)
                                        }
                                    })
                            }
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
                        Resume(context, todoItem.id)
                        Divider(
                            modifier = Modifier.padding(vertical = 5.dp),
                            thickness = 1.dp,
                        )
                        //Adding priority here
                        if (isEditing) {
                            ExposedDropdownMenuBox(
                                expanded = dropDownPriorityExpanded,
                                onExpandedChange = {
                                    dropDownPriorityExpanded = !dropDownPriorityExpanded
                                },
                                modifier = Modifier.padding(2.dp)
                            ) {
                                TextField(
                                    value =
                                    selectedPriority.toString(),
                                    onValueChange = { },
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = dropDownPriorityExpanded
                                        )
                                    },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = dropDownPriorityExpanded,
                                    onDismissRequest = { dropDownPriorityExpanded = false }
                                ) {
                                    PriorityTodosData().forEach { priorityTodos ->
                                        DropdownMenuItem(
                                            text = { Text(priorityTodos.priorityName) },
                                            onClick = {
                                                selectedPriority = priorityTodos.priority
                                                dropDownPriorityExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        } else {
                            Row {
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                                            append("Priority: "+todoItem.priority.toString())
                                        }
                                    })
                            }
                        }

                        val uriCheck = todoItem.imageUri
                        if (uriCheck != null) {
                            val uri = Uri.parse(todoItem.imageUri)
                            if (uri != null) {
                                val imageBitmap = remember(uri) {
                                    try {
                                        ImageDecoder.decodeBitmap(
                                            ImageDecoder.createSource(context.contentResolver, uri)
                                        ).asImageBitmap()
                                    } catch (e: Exception) {
                                        Log.e("Exception", "$e")
                                        null
                                    }
                                }
                                imageBitmap?.let {
                                    Image(it, null)
                                }
                            }
                        }
                        if (isEditing) {
                            Text("Edit Scheduled: ")
                            IconButton(onClick = {
                                isEditingDate = !isEditingDate
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
                            Text("Edit Scheduled : ")

                            IconButton(onClick = {
                                isEditingTime = !isEditingTime
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


                            if (isEditingDate) {
                                DatePickerDialog(
                                    onDismissRequest = { isEditingDate = false },
                                    confirmButton = {
                                        TextButton(onClick = {
                                            val selectedDateMillis =
                                                editDatePickerState.selectedDateMillis
                                            if (selectedDateMillis != null) {
                                                editedScheduledDate =
                                                    handleSelectedDate(selectedDateMillis)
                                            }
                                            isEditingDate = false
                                            snackScope.launch {
                                                snackState.showSnackbar(
                                                    "Selected Date: ${editDatePickerState.selectedDateMillis}"
                                                )
                                            }
                                        }
                                        ) {
                                            Text(text = "Ok")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = { isEditingDate = false }
                                        ) {
                                            Text(text = "Cancel")
                                        }
                                    }
                                ) {
                                    DatePicker(
                                        state = editDatePickerState,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                            if (isEditingTime) {
                                TimePickerDialog(
                                    onCancel = { isEditingTime = false },
                                    onConfirm = {
                                        val cal = Calendar.getInstance()
                                        cal.set(Calendar.HOUR_OF_DAY, editTimePickerState.hour)
                                        cal.set(Calendar.MINUTE, editTimePickerState.minute)
                                        cal.isLenient = false
                                        editedScheduledTime = editTimeFormatter.format(cal.time)
                                        snackScope.launch {
                                            snackState.showSnackbar(
                                                "Entered time: ${
                                                    editTimeFormatter.format(
                                                        cal.time
                                                    )
                                                }"
                                            )
                                        }
                                        isEditingTime = false
                                    }) {
                                    TimePicker(state = editTimePickerState)
                                }
                            }

                        }
                     else {
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
                    }


                    val subtaskTodoViewModel: SubtaskTodoViewModel = viewModel()
                    var subtasksToBeDeleted by remember {
                        mutableStateOf<List<SubtaskTodo>>(
                            emptyList()
                        )
                    }
                    //Subtasks
                    if (subtaskTodoState.isEmpty()) {
                        Text(text = "No subtasks")
                    } else {
                        var editedSubtaskScheduledDate by remember { mutableStateOf("null") }
                        var editedSubtaskScheduledTime by remember { mutableStateOf("null") }
                        var subTaskTitle by remember { mutableStateOf("") }
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

                                var isEditingSubtaskDate by remember { mutableStateOf(false) }
                                var isEditingSubtaskTime by remember { mutableStateOf(false) }
                                val editSubtaskDatePickerState =
                                    rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
                                val editSubtaskTimePickerState = rememberTimePickerState()
                                val subtaskSnackScope = rememberCoroutineScope()
                                val editSubtaskTimeFormatter =
                                    remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
                                val subtaskSnackState = remember { SnackbarHostState() }


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
                                            onCheckedChange = {
                                                isSubtaskChecked = !isSubtaskChecked
                                                subtaskTodoViewModel.setSubtaskFinished(
                                                    subtaskItem.id,
                                                    subtaskItem.subtaskID,
                                                    isSubtaskChecked
                                                )
                                            })
                                        if (isSubtaskChecked) {
                                            subtasksToBeDeleted = subtasksToBeDeleted + subtaskItem
                                        } else {
                                            subtasksToBeDeleted = subtasksToBeDeleted - subtaskItem
                                        }
                                    } else {
                                        Checkbox(
                                            checked = isSubtaskChecked,
                                            onCheckedChange = {
                                                isSubtaskChecked = it
                                                subtaskTodoViewModel.setSubtaskFinished(
                                                    subtaskItem.id,
                                                    subtaskItem.subtaskID,
                                                    isSubtaskChecked
                                                )
                                            })
                                    }
                                    if (isEditing) {
                                        Column {
                                            OutlinedTextField(
                                                value = subtaskEditing,
                                                onValueChange = {
                                                    subtaskEditing = it
                                                    subTaskTitle = subtaskEditing

                                                }
                                            )
                                            Row {
                                                IconButton(onClick = {
                                                    isEditingSubtaskDate = !isEditingSubtaskDate
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
                                                    isEditingSubtaskTime = !isEditingSubtaskTime
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

                                                if (isEditingSubtaskDate) {
                                                    DatePickerDialog(
                                                        onDismissRequest = {
                                                            isEditingSubtaskDate = false
                                                        },
                                                        confirmButton = {
                                                            TextButton(onClick = {
                                                                val selectedDateMillis =
                                                                    editSubtaskDatePickerState.selectedDateMillis
                                                                if (selectedDateMillis != null) {
                                                                    editedSubtaskScheduledDate =
                                                                        handleSelectedDate(
                                                                            selectedDateMillis
                                                                        )
                                                                    subtaskItem.subtaskScheduledDate =
                                                                        editedSubtaskScheduledDate
                                                                }
                                                                isEditingSubtaskDate = false
                                                                subtaskSnackScope.launch {
                                                                    subtaskSnackState.showSnackbar(
                                                                        "Selected Date: ${editSubtaskDatePickerState.selectedDateMillis}"
                                                                    )
                                                                }
                                                            }
                                                            ) {
                                                                Text(text = "Ok")
                                                            }
                                                        },
                                                        dismissButton = {
                                                            TextButton(
                                                                onClick = {
                                                                    isEditingSubtaskDate = false
                                                                }
                                                            ) {
                                                                Text(text = "Cancel")
                                                            }
                                                        }
                                                    ) {
                                                        DatePicker(
                                                            state = editSubtaskDatePickerState,
                                                            modifier = Modifier.padding(8.dp)
                                                        )
                                                    }
                                                }
                                                if (isEditingSubtaskTime) {
                                                    TimePickerDialog(
                                                        onCancel = { isEditingSubtaskTime = false },
                                                        onConfirm = {
                                                            val cal = Calendar.getInstance()
                                                            cal.set(
                                                                Calendar.HOUR_OF_DAY,
                                                                editSubtaskTimePickerState.hour
                                                            )
                                                            cal.set(
                                                                Calendar.MINUTE,
                                                                editSubtaskTimePickerState.minute
                                                            )
                                                            cal.isLenient = false
                                                            editedSubtaskScheduledTime =
                                                                editSubtaskTimeFormatter.format(cal.time)
                                                            subtaskItem.subtaskScheduledTime =
                                                                editedSubtaskScheduledTime
                                                            subtaskSnackScope.launch {
                                                                subtaskSnackState.showSnackbar(
                                                                    "Entered time: ${
                                                                        editSubtaskTimeFormatter.format(
                                                                            cal.time
                                                                        )
                                                                    }"
                                                                )
                                                            }
                                                            isEditingSubtaskTime = false
                                                        }) {
                                                        TimePicker(state = editSubtaskTimePickerState)
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            text = buildAnnotatedString {
                                                withStyle(style = SpanStyle(textDecoration = textDecoration)) {
                                                    append(subtaskItem.subtaskTitle)
                                                    // Add a line break and then append the date and time
                                                    //Date
                                                    if (subtaskItem.subtaskScheduledDate != "null") {
                                                        append("\nScheduled Date: " + subtaskItem.subtaskScheduledDate)
                                                    }
                                                    //Time
                                                    if (subtaskItem.subtaskScheduledTime != "null") {
                                                        append("\nScheduled Time: " + subtaskItem.subtaskScheduledTime)
                                                    }
                                                }
                                            },
                                        )
                                    }
                                }

                                //Updating the subtask
                                if (subtask) {
                                    subtaskTodoViewModel.updateSubtaskTodo(
                                        subtaskItem.copy(
                                            subtaskTitle = subtaskEditing,
                                            subtaskScheduledDate = subtaskItem.subtaskScheduledDate,
                                            subtaskScheduledTime = subtaskItem.subtaskScheduledTime
                                        )
                                    )
                                }
                            }
                        if (editedSubtaskScheduledDate != "null" && editedSubtaskScheduledTime != "null")
                            Notification.SetNotification(
                                editedSubtaskScheduledDate + " " + convertTimeTo24HourFormat(
                                    editedSubtaskScheduledTime
                                ), context, "Notification for todo $subTaskTitle"
                            )
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
                        if (isEditing) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Checked",
                                modifier = Modifier.clickable {
                                    viewModel.updateTodo(
                                        todoItem.copy(
                                            title = editingTitle,
                                            description = editingDescription,
                                            priority = selectedPriority,
                                            scheduledDate = editedScheduledDate,
                                            scheduledTime = editedScheduledTime,

                                            )
                                    )

                                    if (editedScheduledDate != "null" && editedScheduledTime != "null")
                                        Notification.SetNotification(
                                            editedScheduledDate + " " + convertTimeTo24HourFormat(
                                                editedScheduledTime
                                            ), context, "Notification for todo $editingTitle"
                                        )
                                    subtask = true
                                    isEditing = false


                                }
                            )
                        } else {
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

                    Row {
                        if (screenId == 4) {
                            SendTodoToBin(todoItem.id)
                        }
                        if (screenId == 5) {
                            recoverTodoFromBin(todoItem.id)
                            deletePermanently(todoItem.id)
                        }
                    }
                }
                }
            }
        }
    }

}

//Function to delete todos permanently
@Composable
fun deletePermanently(id: Long) {
    val buttonCoroutineScope = rememberCoroutineScope()
    val dao = TodoDatabase.getDatabase(LocalContext.current).todoDao()
    fun clickToDeletePermanently(id: Long) {
        buttonCoroutineScope.launch {
            dao.deleteFromBin(
                id
            )
        }

    }
    IconButton(onClick = {
        clickToDeletePermanently(id)
    }) {
        Icon(Icons.Default.Delete, contentDescription = "Delete")
    }
}

//Function to recover todos from Bin to All todos
@Composable
fun recoverTodoFromBin(id: Long) {
    val buttonCoroutineScope = rememberCoroutineScope()
    val dao = TodoDatabase.getDatabase(LocalContext.current).todoDao()
    fun clickToRecover(id: Long) {
        buttonCoroutineScope.launch {
            dao.setNotDeleted(
                id
            )
        }
    }
    IconButton(onClick = {
        clickToRecover(id)
    }) {
        Icon(Icons.Default.Refresh, contentDescription = "Recover")
    }
}

//Function to send todos to Bin(mark as deleted)
@Composable
fun SendTodoToBin(id: Long){
    val buttonCoroutineScope = rememberCoroutineScope()
    val dao = TodoDatabase.getDatabase(LocalContext.current).todoDao()
    fun clickToDelete(id: Long){
        buttonCoroutineScope.launch {
            dao.delete(
                id
            )
        }
    }
    IconButton(onClick = {
        clickToDelete(id)
    }) {
        Icon(Icons.Default.Delete, contentDescription ="Delete" )
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
        val date: Date = inputFormat.parse(modifiedTime)
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle parsing exception
        return ""
    }
    return ""
}








