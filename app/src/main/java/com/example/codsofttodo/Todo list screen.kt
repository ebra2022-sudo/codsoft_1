package com.example.codsofttodo

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.codsofttodo.data.TimeState
import com.example.codsofttodo.data.ToDoEntry
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TodoScreen( 
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel,
    allProducts: List<ToDoEntry>,
    navController: NavController,
    context: Context
) {
    val postNotificationPermission= rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    val todoNotificationService= TodoNotificationService(context)
    LaunchedEffect(key1 = true ){
        if(!postNotificationPermission.status.isGranted){
            postNotificationPermission.launchPermissionRequest()
        }
    }

    val searchResults = todoViewModel.searchByTitle(todoViewModel.search).observeAsState().value?: emptyList()
    val currentListTypeResult = todoViewModel.searchByListType(todoViewModel.selectedListTypeOnHome).observeAsState().value?: emptyList()
    val completedTodos = todoViewModel.completedTodos().observeAsState().value?: emptyList()
    val listType = todoViewModel.listType.collectAsState().value
    val actions = todoViewModel.actions.collectAsState().value
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    if(todoViewModel.showSearchBar) {
                        Row(modifier = Modifier
                            .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically){
                            IconButton(onClick = {
                                todoViewModel.onShowSearchBar
                                todoViewModel.searchByTitle(todoViewModel.search)
                            }) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary)
                            }
                            // aple of the given vaue of the
                            Column {
                                BasicTextField(
                                    value = todoViewModel.search,
                                    onValueChange = todoViewModel.onSearchChange,
                                    textStyle = TextStyle(fontSize = 20.sp)
                                )
                                HorizontalDivider(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 20.dp))
                            }
                        }
                    }
                    else {
                        Row(
                            modifier = Modifier.fillMaxWidth(0.75f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomDropDownMenu(
                                modifier = Modifier.weight(0.9f),
                                expanded = todoViewModel.showListMenuOnHome,
                                onExpandedChange = todoViewModel.onShowListMenuHome,
                                selectedList = todoViewModel.selectedListTypeOnHome,
                                onDismissedRequest = { todoViewModel.showListMenuOnHome = false },
                                topComposable = {
                                    Row(verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 5.dp)
                                            .clickable {
                                                todoViewModel.selectedListTypeOnHome =
                                                    "All Lists"
                                                todoViewModel.forFilter = false
                                                todoViewModel.forComplete = false
                                                todoViewModel.showListMenuOnHome = false
                                            },
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                        Icon(
                                            imageVector = Icons.Default.Home,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                        Text(text = "All Lists",fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.secondary)
                                    }
                                },
                                bottomComposable = {
                                    Column(modifier= Modifier
                                        .fillMaxWidth()
                                        .padding(start = 5.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    todoViewModel.selectedListTypeOnHome =
                                                        "Finished"
                                                    todoViewModel.forFilter = false
                                                    todoViewModel.forComplete = true
                                                },
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.checkbox_marked),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.secondary
                                            )
                                            Text(text = "Finished", fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.secondary)
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    todoViewModel.showAddListDialogueOnHome = true
                                                },
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)){
                                            NewListDialogue(
                                                showDialogue = todoViewModel.showAddListDialogueOnHome,
                                                onDismiss = {todoViewModel.onShowAddListDialogOnHome},
                                                value = todoViewModel.list,
                                                onValueChange = todoViewModel.onListChange ,
                                                onAdd = {
                                                    todoViewModel.onAddListMenu()
                                                    todoViewModel.showAddListDialogueOnHome = false
                                                todoViewModel.showListMenuOnHome = false}) {
                                                todoViewModel.showAddListDialogueOnHome = false
                                            }
                                            Icon(
                                                painter = painterResource(id = R.drawable.playlist_plus),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.secondary
                                            )
                                            Text(text = "New List",fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.secondary)
                                        }
                                    }

                                },
                                options = listType
                            ) { selectedList ->
                                todoViewModel.selectedListTypeOnHome = selectedList
                                todoViewModel.forFilter = true
                            }
                            IconButton(
                                onClick = { todoViewModel.showListMenuOnHome = true },
                                modifier = Modifier.weight(0.1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary),
                scrollBehavior = scrollBehavior,
                actions = {
                    if (!todoViewModel.showSearchBar) {
                        IconButton(onClick = {
                            todoViewModel.showSearchBar = true
                            todoViewModel.searchByTitle(todoViewModel.search)
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary)
                        }
                        CustomDropDownMenu(
                            expanded = todoViewModel.showMoreAction,
                            onExpandedChange = todoViewModel.onShowMoreAction,
                            selectedList = todoViewModel.selectedActionType,
                            onDismissedRequest = { todoViewModel.showMoreAction = false},
                            options = actions,
                            attachedCompose = {IconButton(onClick = {
                                todoViewModel.showMoreAction = true }) {
                                Icon(imageVector = Icons.Default.MoreVert,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary)
                            }},
                            //modifier = Modifier.fillMaxWidth(0.5f),
                            addLeadingIcon = false
                        ) {selectedAction ->
                            todoViewModel.selectedActionType = selectedAction
                            if (selectedAction == "More Apps") {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse("https://play.google.com/")
                                context.startActivity(intent)
                            }
                        }
                    }
                },
                navigationIcon = { if (todoViewModel.showSearchBar) {
                    IconButton(onClick = {
                        todoViewModel.showSearchBar = false
                        todoViewModel.searchByTitle(todoViewModel.search)
                    }) {
                        Icon(painter = painterResource(id = R.drawable.arrow_left),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
                else IconButton(onClick = {
                    todoNotificationService.showExpandableNotification()
                }) {
                    Icon(painter = painterResource(id = R.drawable.check_circle),
                        contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)}
                })

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("new task")
                    todoViewModel.updateEntryFieldForAdd()
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ){
        todoViewModel.groupedItem = if (todoViewModel.showSearchBar) {
            searchResults
        }
        else if (todoViewModel.forFilter){
            currentListTypeResult
        }
        else if (todoViewModel.forComplete) {
            completedTodos
        }
        else
        {
            allProducts
        }
        LazyColumn(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .then(modifier), verticalArrangement = Arrangement.spacedBy(3.dp)) {

            todoViewModel.groupedItem.groupBy { it.timeState }.forEach {(timeState, entities) ->
                item {
                     // Add a divider for each group
                    Text(text = timeState.name, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 18.sp,
                        color = if (timeState.name == "Overdue") Color.Red else  MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 20.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(20.dp))
                }
                items(entities) { entity ->

                    val formattedDateTime by remember {
                        derivedStateOf {
                            if (entity.setDate != null) {
                                DateTimeFormatter
                                    .ofPattern("EEE, d MMM yyyy.  @h:mm a")
                                    .format(entity.setDate)
                            } else ""
                        }
                    }

                    TodoItem(
                        title = entity.title,
                        timeLine = formattedDateTime,
                        listType = entity.listType,
                        checked = entity.isDone,
                        timeLineColor = if (entity.timeState == TimeState.Overdue) Color.Red else Color.Green,
                        onDelete = {todoViewModel.deleteTodo(entity)},
                        hasRepeat = entity.repeat != "No repeat",
                        onEdit = {
                            todoViewModel.forEdit = true
                            todoViewModel.updateEntryFieldForEdit(entity)
                            todoViewModel.currentToDoEntry = entity
                            navController.navigate("new task")},
                        showListType = entity.listType != "Default",
                        isEdge = if (entity == entities.first()) 1 else if (entity == entities.last()) 3 else 2,
                        showtime = formattedDateTime != null,
                        isChecked = {
                            todoViewModel.currentToDoEntry = entity
                            todoViewModel.onCheck(it)},
                    )
                }
            }
        }
    }
}
@Composable
fun TodoItem(
    modifier: Modifier = Modifier,
    title: String,
    timeLine: String,
    listType: String,
    checked: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    hasRepeat: Boolean = false,
    showListType: Boolean = false,
    showtime: Boolean = false,
    isChecked: (Boolean) -> Unit,
    isEdge: Int = 2,
    timeLineColor: Color = Color.Green,
) {
    var expanded by rememberSaveable {mutableStateOf(false) }
    //this state variable is to remember
    // the state change and recompose all composable that they affected by the state variable.
    val color by animateColorAsState( // the animateColorAsState() function that access different
        //color based on another state variable (expanded)
        targetValue = if (expanded) MaterialTheme.colorScheme.surfaceContainerHighest
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        label = "a"
    )
    Card(modifier = modifier
        .clickable { expanded = !expanded },
        shape = if (isEdge == 1) MaterialTheme.shapes.medium
        else if (isEdge == 3) MaterialTheme.shapes.large
        else MaterialTheme.shapes.small
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = color)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            )) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Checkbox(checked = checked, onCheckedChange = {isChecked(it)})
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically){
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)

                    }

                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)

                    }
                }
            }
            Column(
                modifier = modifier.padding(start = 50.dp, bottom = 5.dp, end = 50.dp)
            ) {
                if (showtime) {
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = timeLine,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(horizontal = 5.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify,
                            color = timeLineColor
                        )
                        if (hasRepeat) {
                            Icon(painter = painterResource(id = R.drawable.autorenew), contentDescription = null, tint = Color.Yellow)
                        }
                    }
                }
                if (showListType) {
                    Text(
                        text = listType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenSetUp(mainViewModel: TodoViewModel, navController: NavController, context: Context) {
    val allProducts by mainViewModel.todos.observeAsState(listOf())
    TodoScreen(todoViewModel = mainViewModel, allProducts = allProducts, navController = navController, context = context)
}








