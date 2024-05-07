package com.example.codsofttodo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.codsofttodo.data.ToDoEntry


// Data Access Objects are the main classes where you define your database interactions(Interfaces).
// They can include a variety of query methods(such as insert, delete, searching, updating
// entry value(column features or properties of the @entity annotated class), ..).
// Data access objects are implemented only using interface od abstract data class
// in this interface or abstract class you cannot implement the function, rather define the signature.


// the @Dao annotation list api for the room database
@Dao
interface DataBaseOperations {
    //These annotations allow Room to generate the necessary
    // code to perform database operations associated with database entry

    @Insert(onConflict = OnConflictStrategy.REPLACE) // to update the todo if it exists already
    fun insertTodo(toDo: ToDoEntry)

    @Delete
    fun deleteToDo(toDo: ToDoEntry)

    @Update
    fun update(toDo: ToDoEntry)

    // search by id
    @Query("SELECT * FROM Todos WHERE id = :id")
    fun selectToDoById(id: Int): List<ToDoEntry>

    // search by title
    @Query("SELECT * FROM Todos WHERE title =:title")
    fun selectToDoByTitle(title: String): List<ToDoEntry>

    // getting all todos
    @Query("SELECT * FROM Todos")
    fun getAllTodos(): LiveData<List<ToDoEntry>>

}