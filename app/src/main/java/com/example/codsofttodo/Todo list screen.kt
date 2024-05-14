package com.example.codsofttodo

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.codsofttodo.data.TimeState
import com.example.codsofttodo.data.ToDoEntry
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen( 
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel,
    allProducts: List<ToDoEntry>,
    navController: NavController,
) {
    val searchResults = todoViewModel.searchByTitle(todoViewModel.search).observeAsState().value?: emptyList()
    val currentListTypeResult = todoViewModel.searchByListType(todoViewModel.selectedListTypeOnHome).observeAsState().value?: emptyList()
    val completedTodos = todoViewModel.completedTodos().observeAsState().value?: emptyList()
    val listType = todoViewModel.listType.collectAsState().value
    val actions = todoViewModel.actions.collectAsState().value
    val context = LocalContext.current
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
                    if (todoViewModel.forShare) {
                            IconButton(onClick = {
                                shareTodo(
                                    context = context,
                                    subject = todoViewModel.currentSharedContent)

                                todoViewModel.forShare = false}) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary)
                            }
                    }
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
                            textColor = MaterialTheme.colorScheme.onSecondary,
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
                            if (selectedAction == "Task Lists") {
                                navController.navigate("Task lists screen")
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

            todoViewModel.groupedItem.groupBy {todo -> todo.timeState }.forEach {(timeState, entities) ->
                item {
                     // Add a divider for each group
                    Text(text = timeState.name, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 18.sp,
                        color = if (timeState.name == "Overdue") Color.Red else  MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 20.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(20.dp))
                }
                items(entities) { entity ->

                    val formattedDateTime by remember(entity.setDate) {
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
                        onTap = {todoViewModel.forShare = false},
                        onEdit = {
                            todoViewModel.forEdit = true
                            todoViewModel.updateEntryFieldForEdit(entity)
                            todoViewModel.currentToDoEntry = entity
                            navController.navigate("new task")},
                        showListType = entity.listType != "Default",
                        isEdge = if (entities.size == 1) 4 else if (entity == entities.first())
                            1 else if (entity == entities.last()) 3 else 2,
                        showtime = formattedDateTime != null,
                        isChecked = {check ->
                            todoViewModel.currentToDoEntry = entity
                            todoViewModel.onCheck(check)},
                        onLongPress = {
                            todoViewModel.currentSharedContent = "${entity.title}\n$formattedDateTime"
                            todoViewModel.forShare = true}
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenSetUp(mainViewModel: TodoViewModel, navController: NavController) {
    val allProducts by mainViewModel.todos.observeAsState(listOf())
    TodoScreen(todoViewModel = mainViewModel, allProducts = allProducts, navController = navController)
}








