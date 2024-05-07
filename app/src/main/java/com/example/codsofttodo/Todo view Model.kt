package com.example.codsofttodo

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.codsofttodo.data.TimeState
import com.example.codsofttodo.data.ToDoEntry
import com.example.codsofttodo.data.TodoRepository
import com.example.codsofttodo.data.TodoRoomDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TodoViewModel(application: Application): ViewModel(){
    val todos: LiveData<List<ToDoEntry>>
    private val repository: TodoRepository
    private var _title by mutableStateOf("")
    var showListMenuOnHome by mutableStateOf(false)
    var showListMenuOnNewTask by mutableStateOf(false)
    var showAddListDialogueOnNewTask by mutableStateOf(false)
    var showAddListDialogueOnHome by mutableStateOf(false)
    var showMoreAction by mutableStateOf(false)
    var showRepeatMenu by mutableStateOf(false)
    var currentToDoEntry by mutableStateOf(ToDoEntry(title= "", setDate = "",
        isDone = false, timeState = TimeState.NoDate, repeat = "", listType = ""))

    var forEdit by   mutableStateOf(false)




    private val _listType = MutableStateFlow(mutableListOf("Default", "Completed"))
    val listType: StateFlow<MutableList<String>>
        get() = _listType.asStateFlow()

    private val _repeats =
        MutableStateFlow(mutableListOf("No repeat", "Once a Day",
            "Once a week", "Once a Month", "Once a Year"))
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
    var pickedDateTime by mutableStateOf<LocalDateTime?>(null)




    val onShowListMenuHome = {flag: Boolean -> showListMenuOnHome = !showListMenuOnHome}
    val onShowListMenuNewTask = {flag: Boolean -> showListMenuOnNewTask = !showListMenuOnNewTask}
    val onShowRepeatMenu = {flag: Boolean -> showRepeatMenu = !showRepeatMenu}
    val onShowMoreAction = {flag: Boolean -> showMoreAction = !showMoreAction}
    val onShowAddListDialogOnNewTask = {showAddListDialogueOnNewTask =!showAddListDialogueOnNewTask}



    val title: String
        get() = _title
    val onTitleChange = { new: String -> _title = new }

    private var _list by mutableStateOf("")
    val list: String
        get() = _list
    val onListChange = { new: String -> _list = new }
    fun onAddListMenu() {
        _listType.value.add(list.trim())
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

    fun insertTodo() {
        val newToDoEntry = ToDoEntry(
            title = title,
            setDate = pickedDateTime.toString(),
            isDone = false,
            repeat = selectedRepeatType,
            listType = selectedListTypeOnNewTask,
            timeState = timeState(pickedDateTime?.toLocalDate())
            )
        repository.insertTodo(newToDoEntry)
    }

    private fun timeState(date: LocalDate?): TimeState {
        return when {
            date == null -> TimeState.NoDate
            date.isEqual(LocalDate.now()) -> TimeState.Today
            date.isEqual(LocalDate.now().plusDays(1)) -> TimeState.Tomorrow
            date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusWeeks(1)) -> TimeState.NextWeek
            date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusMonths(1)) -> TimeState.NextMonth
            date.isAfter(LocalDate.now()) -> TimeState.Later
            else -> TimeState.Overdue
        }
    }

    fun mergeTimeSndDate(date: LocalDate? = null, time: LocalTime? = null): LocalDateTime? {
        return LocalDateTime.of(date, time)
    }



    fun deleteTodo(toDoEntry: ToDoEntry) {
        repository.deleteTodo(toDoEntry)
    }
    fun updateEntryField(toDoEntry: ToDoEntry) {
        _title = toDoEntry.title
        selectedRepeatType = toDoEntry.repeat
    }
    fun onEdit() {
        repository.updateEntityField(
            entityId = currentToDoEntry.id,
            title = title,
            setDate = pickedDateTime,
            timeState= timeState(pickedDateTime?.toLocalDate()),
            repeat = selectedRepeatType
        )
    }

    fun searchByTitle(title: String): MutableLiveData<List<ToDoEntry>> {
        repository.searchByName(title)
        return repository.searchResults
    }
}