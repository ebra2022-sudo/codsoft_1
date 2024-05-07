package com.example.codsofttodo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TodoRepository(private val databaseOperations: DataBaseOperations) {
    val searchResults = MutableLiveData<List<ToDoEntry>>()

    // to access the updated database records.
    val allTodos: LiveData<List<ToDoEntry>> = databaseOperations.getAllTodos()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun insertTodo(newToDoEntry: ToDoEntry) {
        coroutineScope.launch(Dispatchers.IO) {
            databaseOperations.insertTodo(newToDoEntry)
        }
    }

    fun deleteTodo(toDoEntry: ToDoEntry) {
        coroutineScope.launch(Dispatchers.IO) {
            databaseOperations.deleteToDo(toDoEntry)
        }
    }

    fun searchByName(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(name).await()
        }
    }


    fun updateEntityField(
        entityId: Int,
        title: String,
        setDate: LocalDateTime?,
        timeState: TimeState,
        repeat: String ) {
        coroutineScope.launch(Dispatchers.IO) {
            // Retrieve the entity from the database
            var entity = databaseOperations.selectToDoById(entityId).first()

            // Modify the field value of the retrieved entity
            entity = entity.copy(
                title = title,
                setDate = setDate.toString(),
                timeState = timeState,
                repeat = repeat)

            // Update the entity in the database with the modified field value
            databaseOperations.update(entity)
        }
        // SAMPLE OF THE GIVEN VALUE OF THE CODE OF THE SAMPLE OF THE GV
    }

    fun onCheckTodo(entityId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            var entity = databaseOperations.selectToDoById(entityId).first()
            entity = entity.copy(isDone = !entity.isDone)
            databaseOperations.update(entity)
        }
    }
    // generic function for search async
    private fun <T> asyncFind(key: T): Deferred<List<ToDoEntry>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async when (key) {
                is String -> databaseOperations.selectToDoByTitle(key)
                else -> databaseOperations.selectToDoById(key as Int)
            }
        }
}
