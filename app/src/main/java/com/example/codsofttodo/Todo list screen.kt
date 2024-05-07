package com.example.codsofttodo

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.codsofttodo.data.TimeState
import com.example.codsofttodo.data.ToDoEntry


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    todoViewModel: TodoViewModel,
    allProducts: List<ToDoEntry>,
    navController: NavController
) {
    val searchResults = todoViewModel.searchByTitle(todoViewModel.search).observeAsState().value?: emptyList()
    val listType = todoViewModel.listType.collectAsState().value
    val actions = todoViewModel.actions.collectAsState().value
    var showSearchBar by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    if(showSearchBar) {
                        Row(modifier = Modifier
                            .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically){
                            IconButton(onClick = {
                                showSearchBar = !showSearchBar
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
                                                    todoViewModel.showListMenuOnHome = false
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
                                                .clickable { todoViewModel.showAddListDialogueOnHome },
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)){
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
                    if (!showSearchBar) {
                        IconButton(onClick = {
                            showSearchBar = true
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
                        }
                    }
                },
                navigationIcon = { if (showSearchBar) {
                    IconButton(onClick = {
                        showSearchBar = false
                        todoViewModel.searchByTitle(todoViewModel.search)
                    }) {
                        Icon(painter = painterResource(id = R.drawable.arrow_left),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }

                }
                else Icon(painter = painterResource(id = R.drawable.check_circle),
                    contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)})

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("new task")
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ){
        var groupedItem by remember { mutableStateOf(allProducts.groupBy { toDo: ToDoEntry -> toDo.timeState }) }
        if (showSearchBar) {
            groupedItem = searchResults.groupBy { toDo: ToDoEntry -> toDo.timeState }
        }
        else allProducts.groupBy {toDo: ToDoEntry -> toDo.timeState}
        LazyColumn(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .then(modifier), verticalArrangement = Arrangement.spacedBy(10.dp),
            reverseLayout = true) {


            groupedItem.forEach {(timeState, entities) ->
                item {
                    HorizontalDivider() // Add a divider for each group
                    Text(text = timeState.name)
                }
                items(entities) { entity ->
                    TodoItem(
                        title = entity.title,
                        timeLine = entity.setDate,
                        listType = entity.listType,
                        checked = entity.isDone,
                        timeLineColor = if (entity.timeState == TimeState.Overdue) Color.Red else Color.Green,
                        onDelete = {todoViewModel.deleteTodo(entity)},
                        onEdit = {
                            todoViewModel.forEdit = true
                            todoViewModel.updateEntryField(entity)
                            todoViewModel.currentToDoEntry = entity
                            navController.navigate("new task")},
                        showListType = entity.listType != "Default",
                        showtime = true,
                        isChecked = {todoViewModel.onCheck(entity)},
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
    showListType: Boolean = false,
    showtime: Boolean = false,
    isChecked: (Boolean) -> Unit,
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
        shape = MaterialTheme.shapes.medium

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
                    Checkbox(checked = checked, onCheckedChange = isChecked)
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
                modifier = modifier.padding(start = 50.dp, bottom = 5.dp)
            ) {
                if (showtime) {
                    Text(
                        text = timeLine,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify,
                        color = timeLineColor
                    )
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
fun ScreenSetUp(mainViewModel: TodoViewModel, navController: NavController) {
    val allProducts by mainViewModel.todos.observeAsState(listOf())
    TodoScreen(todoViewModel = mainViewModel, allProducts = allProducts, navController = navController)
}








