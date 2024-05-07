package com.example.codsofttodo.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room


@Database(
    entities = [ToDoEntry:: class],
    version = 2
)

abstract class TodoRoomDatabase: RoomDatabase()
{
    abstract fun  databaseOperations(): DataBaseOperations
    // we want singleton object(one instance of room database)
    // so we have to check  whether it is already created  or not.
    companion object {
        private var INSTANCE: TodoRoomDatabase? = null
        fun getInstance(context: Context):
                TodoRoomDatabase {synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        TodoRoomDatabase::class.java, "Todo_database").
                    fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
