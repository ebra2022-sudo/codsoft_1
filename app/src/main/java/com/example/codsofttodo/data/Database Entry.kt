package com.example.codsofttodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
@TypeConverters(LocalDateTimeConverter::class)
data class ToDoEntry(
    var title: String,
    var setDate:LocalDateTime?,
    var isDone: Boolean,
    var timeState: TimeState,
    var repeat: String,
    var listType: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)


enum class TimeState {
    NoDate, Overdue,Today, Tomorrow,ThisWeek, NextWeek, NextMonth, Later
}

class LocalDateTimeConverter {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }
}



