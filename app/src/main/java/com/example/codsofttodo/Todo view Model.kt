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

class TodoViewModel(application: Application): ViewModel(){
    val todos: LiveData<List<ToDoEntry>>
    private val repository: TodoRepository
    private var _title by mutableStateOf("")
    private var _currentId by mutableIntStateOf(0)
    val title: String
        get() = _title
    val onTitleChange = { new: String -> _title = new }

    private var _search by mutableStateOf("")
    val search: String
        get() = _search
    val onSearchChange = { new: String -> _search = new }

    private var _description by mutableStateOf("")
    val description: String
        get() = _description
    val onDescriptionChange = { new: String -> _description= new }

    private var _duration by mutableStateOf("0")
    val duration: String
        get() = _duration
    val onDurationChange = { new: String -> _duration = new }

    var showBottomSheet by  mutableStateOf(false)
    fun openBottomSheet() {showBottomSheet = true}
    fun closeBottomSheet() {showBottomSheet = false}

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
        val newToDoEntry = ToDoEntry(title = title, description = description,
            isDone = false, duration = if (isValidDuration(duration)) duration.toInt() else 0)
        repository.insertTodo(newToDoEntry)
    }

    fun deleteTodo(toDoEntry: ToDoEntry) {
        repository.deleteTodo(toDoEntry)
    }
    fun updateEntryField(toDoEntry: ToDoEntry){
        _title = toDoEntry.title
        _description = toDoEntry.description?:""
        _duration= toDoEntry.duration.toString()
        _currentId = toDoEntry.id
    }
    fun onEdit() {
        repository.updateEntityField(entityId = _currentId, title = _title,
            description = _description, duration = _duration.toInt())
    }

    fun searchByTitle(title: String): MutableLiveData<List<ToDoEntry>> {
        repository.searchByName(title)
        return repository.searchResults
    }

}