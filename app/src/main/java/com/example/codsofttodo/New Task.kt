package com.example.codsofttodo

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskScreen(navController: NavController, todoViewModel: TodoViewModel) {
    val listType = todoViewModel.listType.collectAsState().value
    val repeats = todoViewModel.repeats.collectAsState().value
    Scaffold(
        topBar = {
        TopAppBar(
            title = {Text("New Task", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)},
            navigationIcon = {
                IconButton(onClick = { navController.navigate("todo list screen") }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)
            )
                 },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (todoViewModel.forEdit) {
                        todoViewModel.onEdit()
                        todoViewModel.forEdit = false
                    }
                    else todoViewModel.insertTodo()
                    navController.navigate("todo list Screen")
                }
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
            }
        }) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(it)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)) {



            val dateDialogState = rememberMaterialDialogState()
            val currentDate = remember { LocalDate.now() }
            var pickedDate by remember {mutableStateOf(LocalDate.now())}
            val dateColor by animateColorAsState(
                targetValue = if (pickedDate != null && currentDate.isAfter(pickedDate)) Color.Red
                else MaterialTheme.colorScheme.onPrimaryContainer,
                label = "date color indicator"
            )
            val formattedDate by remember {
                derivedStateOf {
                    DateTimeFormatter
                        .ofPattern("MMM dd yyyy")
                        .format(pickedDate)
                }
            }

            MaterialDialog(
                dialogState = dateDialogState,
                buttons = {
                    positiveButton(text = "Ok") {
                        todoViewModel.date = formattedDate
                        LocalDate.of(pickedDate.year, pickedDate.month, pickedDate.dayOfMonth)
                    }
                    negativeButton(text = "Cancel")
                }
            ) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "Pick a date"
                ) {
                    localDate ->
                    pickedDate = localDate
                    todoViewModel.pickedDateTime = todoViewModel.mergeTimeSndDate(date = localDate, time = LocalTime.now())
                }
            }

            val timePickerState = rememberTimePickerState(is24Hour = false)
            val showTimePicker = remember { mutableStateOf(false) }
            val currentTime = remember { LocalTime.now() }
            val setTime = remember { mutableStateOf<LocalTime?>(null) }
            val timeColor by animateColorAsState(
                targetValue = if (dateColor == Color.Red) Color.Red
                else if ((setTime.value != null && currentTime.isAfter(setTime.value)) && currentDate.isEqual(pickedDate)) Color.Red
                else MaterialTheme.colorScheme.onPrimaryContainer,
                label = "date color indicator"
            )
            if (showTimePicker.value) {
                TimePickerDialog(
                    onCancel = { showTimePicker.value = false },
                    onConfirm = {
                        setTime.value = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        todoViewModel.pickedDateTime =
                            todoViewModel.mergeTimeSndDate(date = pickedDate, time =LocalTime.of(timePickerState.hour, timePickerState.minute))
                        showTimePicker.value = false },
                    content = {displayMode ->
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        calendar.set(Calendar.MINUTE, timePickerState.minute)
                        todoViewModel.time = calendar.time.time.toTime()
                         if (displayMode == DisplayMode.Input) {
                            TimePicker(state = timePickerState)
                        } else {
                            TimeInput(state = timePickerState)
                        }
                    })
            }
            Column {
                Text(text = "What is to be done?", fontWeight = FontWeight.Medium)
                Row(modifier = Modifier.fillMaxWidth()){
                    TextField(value = todoViewModel.title,
                        onValueChange = todoViewModel.onTitleChange,
                        modifier = Modifier.weight(0.9f),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        label = {
                            Text(
                                text = "Enter Task Here",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(painter = painterResource(id = R.drawable.microphone_settings), contentDescription = null)
                    }
                }
            }
            Column {
                Text(text = "Due date",fontWeight = FontWeight.Medium)
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { dateDialogState.show() },
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Column(modifier = Modifier.weight(0.9f)) {
                        Text(
                            text = todoViewModel.date,
                            modifier = Modifier.fillMaxWidth(),
                            color = dateColor
                        )
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    IconButton(onClick = {dateDialogState.show()} ) {
                        Icon(painter = painterResource(id = R.drawable.calendar),
                            contentDescription = null,
                            modifier = Modifier.weight(0.1f))
                    }
                    if (todoViewModel.date.last().isDigit()) {
                        IconButton(onClick = {todoViewModel.date = "Date not set"} ) {
                            Icon(painter = painterResource(id = R.drawable.close_circle),
                                contentDescription = null,
                                modifier = Modifier.weight(0.1f))
                        }
                    }
                }
                if (todoViewModel.date.last().isDigit()) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker.value = true },
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Column(modifier = Modifier.weight(0.9f)) {
                            Text(
                                text = todoViewModel.time,
                                modifier = Modifier.fillMaxWidth(),
                                color = timeColor
                            )
                            HorizontalDivider(modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        IconButton(onClick = {showTimePicker.value = true} ) {
                            Icon(painter = painterResource(id = R.drawable.clock_outline),
                                contentDescription = null,
                                modifier = Modifier.weight(0.1f))
                        }
                        if (todoViewModel.time.first().isDigit()) {
                            IconButton(onClick = {todoViewModel.time = "Time not set"} ) {
                                Icon(painter = painterResource(id = R.drawable.close_circle),
                                    contentDescription = null,
                                    modifier = Modifier.weight(0.1f))
                            }
                        }
                    }
                }
                Text(text = "Notification",textDecoration = TextDecoration.Underline, color = Color.Magenta)
                Text(text = "No notification if date not set")

            }
            if (todoViewModel.date.last().isDigit()) {
                Column {
                    Text(text = "Repeats", fontWeight = FontWeight.Medium)
                    Row(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CustomDropDownMenu(
                            expanded = todoViewModel.showRepeatMenu,
                            onExpandedChange = todoViewModel.onShowRepeatMenu,
                            selectedList = todoViewModel.selectedRepeatType,
                            onDismissedRequest = { todoViewModel.showRepeatMenu = false},
                            modifier = Modifier.weight(5f),
                            options = repeats,
                            addLeadingIcon = false
                        ) {selectedList ->
                            todoViewModel.selectedRepeatType = selectedList
                        }
                        IconButton(
                            onClick = { todoViewModel.showRepeatMenu = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    }
                }

            }
            Column {
                Text(text = "Add to List", fontWeight = FontWeight.Medium)
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    CustomDropDownMenu(
                        expanded = todoViewModel.showListMenuOnNewTask,
                        onExpandedChange = todoViewModel.onShowListMenuNewTask,
                        selectedList = todoViewModel.selectedListTypeOnNewTask,
                        onDismissedRequest = { todoViewModel.showListMenuOnNewTask = false },
                        modifier = Modifier.weight(4f),
                        options = listType
                    ) {selectedList ->
                        todoViewModel.selectedListTypeOnNewTask = selectedList
                    }

                    NewListDialogue(
                        showDialogue = todoViewModel.showAddListDialogueOnNewTask,
                        onDismiss = {todoViewModel.onShowAddListDialogOnNewTask},
                        value = todoViewModel.list,
                        onValueChange = todoViewModel.onListChange ,
                        onAdd = {
                            todoViewModel.onAddListMenu()
                            todoViewModel.showAddListDialogueOnNewTask = false})
                    {todoViewModel.showAddListDialogueOnNewTask = false}
                    IconButton(onClick = {todoViewModel.showListMenuOnNewTask = true}, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                    IconButton(onClick = todoViewModel.onShowAddListDialogOnNewTask, modifier = Modifier.weight(1f)) {
                        Icon(
                            painter = painterResource(id = R.drawable.playlist_plus),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable (DisplayMode) -> Unit,
) {
    val displayModeState = remember { mutableStateOf(DisplayMode.Input) }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
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
                )
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
                content(displayModeState.value)
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    DisplayModeToggleButton(displayModeState = displayModeState)

                    Spacer(modifier = Modifier.weight(1F))
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisplayModeToggleButton(
    displayModeState: MutableState<DisplayMode>,
    modifier: Modifier = Modifier,
) {
    when (displayModeState.value) {
        DisplayMode.Picker -> IconButton(
            modifier = modifier,
            onClick = { displayModeState.value = DisplayMode.Input },
        ) {
            Icon(
                painter = painterResource(R.drawable.clock_outline),
                contentDescription = "time_picker_button_select_input_mode"
            )
        }
        DisplayMode.Input -> IconButton(
            modifier = modifier,
            onClick = { displayModeState.value = DisplayMode.Picker },
        ) {
            Icon(
                painter = painterResource(R.drawable.keyboard),
                contentDescription = "time_picker_button_select_picker_mode"
            )
        }
    }
}

@Composable
fun NewListDialogue(
    showDialogue: Boolean,
    onDismiss: ()-> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
    onCancel: () -> Unit
) {
    CustomDialog(
        showDialog = showDialogue,
        onDismissRequest = onDismiss) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(20.dp)) {
            Text(text = "New List", modifier = Modifier.align(Alignment.TopStart))
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                keyboardActions = KeyboardActions(onDone = {onAdd()}),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                label = {Text(text = "Enter new list")})
            Row(modifier = Modifier.align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "CANCEL", modifier = Modifier.clickable { onCancel()})
                Text(text = "ADD", modifier = Modifier.clickable(onClick = {onAdd()}))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedList: String,
    onDismissedRequest: ()->Unit,
    options: MutableList<String>,
    addLeadingIcon: Boolean = true,
    topComposable: @Composable () -> Unit = {},
    bottomComposable: @Composable () -> Unit = {},
    attachedCompose: @Composable () -> Unit = {
        Text(
            text = selectedList,
            modifier = Modifier
                .fillMaxWidth()
        )
    },
    onItemClicked: (String)-> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        Box(modifier = Modifier.menuAnchor()) {
            attachedCompose()
        }
        ExposedDropdownMenu(expanded = expanded,
            onDismissRequest = onDismissedRequest,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .width(200.dp)) {
            topComposable()
            options.toSet().forEach { option: String ->
                DropdownMenuItem(
                    text = { Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxSize()
                            .background(color = if (selectedList == option) MaterialTheme.colorScheme.secondary else Color.Transparent)) {
                        if (addLeadingIcon) Icon(painter = painterResource(id = R.drawable.menu), contentDescription =null )
                        Text(text = option, fontWeight = FontWeight.Medium)
                    } },
                    onClick = {
                        onItemClicked(option)
                        onDismissedRequest()
                    },
                    colors =
                    if (selectedList == option)
                        MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.onSecondary)
                    else MenuDefaults.itemColors(MaterialTheme.colorScheme.secondary)
                )
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
            bottomComposable()
        }
    }
}






