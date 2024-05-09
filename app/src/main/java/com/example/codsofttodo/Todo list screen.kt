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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todolist.data.ToDoEntry


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
    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    //var forEdit by rememberSavable { mutableStateOf(false) }
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
                         containerColor = MaterialTheme.colorScheme.primary, titleContentColor = MaterialTheme.colorScheme.onPrimary),
                     scrollBehavior = scrollBehavior,
                     actions = {
                         if (!showSearchBar) {
                             IconButton(onClick = {
                                 showSearchBar = !showSearchBar
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
                                     Icon(imageVector = Icons.Default.MoreVert, contentDescription = null,
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
                             showSearchBar = !showSearchBar
                             todoViewModel.searchByTitle(todoViewModel.search)
                         }) {
                             Icon(painter = painterResource(id = R.drawable.arrow_left), contentDescription = null,
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
        LazyColumn(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .padding(horizontal = 5.dp)
            .then(modifier), verticalArrangement = Arrangement.spacedBy(10.dp),
            reverseLayout = true) {
            item {Spacer(modifier = Modifier.height(1.dp))}
            items(if (showSearchBar) searchResults else allProducts) {entity ->
                TodoItem(
                    title = entity.title,
                    description = entity.description?:"",
                    checked = entity.isDone,
                    isChecked = {todoViewModel.onCheck(entity)},
                    onDelete = {todoViewModel.deleteTodo(entity)},
                    onEdit = {})
            }
        }
    }
}
@Composable
fun TodoItem(
    title: String,
    description: String,
    checked: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    isChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
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
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Checkbox(checked = checked, onCheckedChange = isChecked)
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
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
            if (expanded) {
                Column(
                    modifier = modifier.padding(start = 15.dp, bottom = 5.dp)
                ) {
                    Text(
                        text = description,
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








