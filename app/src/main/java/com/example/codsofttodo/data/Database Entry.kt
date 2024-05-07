package com.example.codsofttodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

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
    var setDate:String,
    var isDone: Boolean,
    var timeState: TimeState,
    var repeat: String,
    var listType: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

enum class TimeState {
    Overdue, Today, Tomorrow,NextWeek, NextMonth, Later, NoDate
}

