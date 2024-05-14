package com.example.codsofttodo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.codsofttodo.data.TimeState
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskListsScreen(todoViewModel: TodoViewModel, navController: NavController) {
    val todos = todoViewModel.todos.observeAsState(emptyList()).value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Task Lists",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )},
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("todo list Screen") }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null)
                    }
                },
                actions = {
                    if (todoViewModel.forDeleteList) {
                        IconButton(onClick = {
                            todoViewModel.onRemoveListMenu()
                            todoViewModel.forDeleteList = false}) {
                            Icon(imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Red)
                        }
                    }
                })
        },
    ) {
        LazyColumn(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)) {

            todos.groupBy { todo -> todo.listType }.forEach { (listType, entities) ->
                item {
                    // Add a divider for each group
                    Text(text = listType, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 18.sp,
                        color = Color(0xFFFF00FF),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .combinedClickable(
                                onClick = { todoViewModel.forDeleteList = false},
                                onLongClick = {
                                    todoViewModel.currentDeletedList = listType
                                    todoViewModel.forDeleteList = true },
                                onLongClickLabel = null
                            ))
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
                        onEdit = {
                            todoViewModel.forEdit = true
                            todoViewModel.updateEntryFieldForEdit(entity)
                            todoViewModel.currentToDoEntry = entity
                            navController.navigate("new task")},
                        showListType = entity.listType != "Default",
                        isEdge = if (entities.size == 1) 4 else if (entity == entities.first())
                            1 else if (entity == entities.last()) 3 else 2,showtime = formattedDateTime != null,
                        isChecked = {check ->
                            todoViewModel.currentToDoEntry = entity
                            todoViewModel.onCheck(check)},
                    )
                }
            }
        }
    }
}