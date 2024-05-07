package com.example.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/*Entity Annotation: The @Entity annotation on the TodoEntry class
 informs Room to recognize this data class as a database table(Raw). Its
 properties are interpreted as column names(features), with the class name
 defaulting as the table(RAW) name unless explicitly changed.
 */

/*Primary Key with Auto-Generate: Annotating the id field with
 @PrimaryKey(autoGenerate = true) designates it as the unique identifier
 for each table row.
 */
@Entity(tableName = "Todos")
data class ToDoEntry(
    var title: String,
    var description: String?,
    var isDone: Boolean,
    var duration: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)