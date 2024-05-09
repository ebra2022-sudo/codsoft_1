package com.example.codsofttodo

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.data.ToDoEntry
import com.example.todolist.data.TodoRepository
import com.example.todolist.data.TodoRoomDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoViewModel(application: Application): ViewModel(){
    val todos: LiveData<List<ToDoEntry>>
    private val repository: TodoRepository
    private var _title by mutableStateOf("")
    private var _currentId by mutableIntStateOf(0)
    var showListMenuOnHome by mutableStateOf(false)
    var showListMenuOnNewTask by mutableStateOf(false)
    var showAddListDialogueOnNewTask by mutableStateOf(false)
    var showAddListDialogueOnHome by mutableStateOf(false)
    var showAddListDialogueOnAction by mutableStateOf(false)
    var showMoreAction by mutableStateOf(false)
    var showRepeatMenu by mutableStateOf(false)
    var showAddListDialog by mutableStateOf(false)


    private val _listType = MutableStateFlow(mutableListOf("Default", "Completed"))
    val listType: StateFlow<MutableList<String>>
        get() = _listType.asStateFlow()

    private val _repeats =
        MutableStateFlow(mutableListOf("No repeat", "Once a Day","Once a Day(Mon -Fri)",
            "Once a week", "Once a Month", "Once a Year","Other..."))
    val repeats: StateFlow<MutableList<String>>
        get() = _repeats.asStateFlow()

    private val _actions =
        MutableStateFlow(mutableListOf("Task Lists", "Add in Batch Mode","Remove Ads",
            "More Apps", "Send feedback", "Follow us","Invite friends to the app", "Settings"))
    val actions: StateFlow<MutableList<String>>
        get() = _actions.asStateFlow()

    var selectedListTypeOnNewTask by mutableStateOf(_listType.value[0])
    var selectedListTypeOnHome by mutableStateOf(_listType.value[0])
    var selectedRepeatType by mutableStateOf(_repeats.value[0])
    var selectedActionType by mutableStateOf(_actions.value[0])


    val onShowListMenuHome = {flag: Boolean -> showListMenuOnHome = !showListMenuOnHome}
    val onShowListMenuNewTask = {flag: Boolean -> showListMenuOnNewTask = !showListMenuOnNewTask}
    val onShowRepeatMenu = {flag: Boolean -> showRepeatMenu = !showRepeatMenu}
    val onShowMoreAction = {flag: Boolean -> showMoreAction = !showMoreAction}
    val onShowAddListDialog = {showAddListDialog = !showAddListDialog}
    val onShowAddListDialogOnNewTask = {showAddListDialogueOnNewTask =!showAddListDialogueOnNewTask}


    val title: String
        get() = _title
    val onTitleChange = { new: String -> _title = new }

    private var _list by mutableStateOf("")
    val list: String
        get() = _list
    val onListChange = { new: String -> _list = new }
    fun onAddListMenu() {
        _listType.value.add(list)
    }

    private var _search by mutableStateOf("")
    val search: String
        get() = _search
    val onSearchChange = { new: String -> _search = new }

    var date by mutableStateOf("Date not set")
    var time by mutableStateOf("Time not set")


    private var _duration by mutableStateOf("0")


    fun onCheck(current: ToDoEntry) {
        repository.onCheckTodo(current.id)
    }

    init {
        val todoDb = TodoRoomDatabase.getInstance(application)
        val todoDao = todoDb.databaseOperations()
        repository = TodoRepository(todoDao)
        todos = repository.allTodos
    }
    private fun isValidDuration(s: String): Boolean  {
        val isValid = true
        for (i in s) {
            if (!i.isDigit()) return false
        }
        return isValid
    }
    /*

    fun insertTodo() {
        val newToDoEntry = ToDoEntry(title = title, description = date,
            isDone = false, duration = if (isValidDuration(duration)) duration.toInt() else 0)
        repository.insertTodo(newToDoEntry)
    }

     */

    fun deleteTodo(toDoEntry: ToDoEntry) {
        repository.deleteTodo(toDoEntry)
    }
    fun updateEntryField(toDoEntry: ToDoEntry){
        _title = toDoEntry.title
        date = toDoEntry.description?:""
        _duration= toDoEntry.duration.toString()
        _currentId = toDoEntry.id
    }
    fun onEdit() {
        repository.updateEntityField(entityId = _currentId, title = _title,
            description =date, duration = _duration.toInt())
    }

    fun searchByTitle(title: String): MutableLiveData<List<ToDoEntry>> {
        repository.searchByName(title)
        return repository.searchResults
    }

}