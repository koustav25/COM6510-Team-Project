package com.example.myapplication.todoDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.todoDao.SubtaskTodoDao
import com.example.myapplication.todoDao.TodoDao
import com.example.myapplication.todoEntities.SubtaskTodo
import com.example.myapplication.todoEntities.Todo

@Database(entities = [Todo::class, SubtaskTodo::class], version = 2, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao
    abstract fun subtaskDao(): SubtaskTodoDao

    companion object {
        private const val DB_NAME = "Todo_DB"
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context,
                TodoDatabase::class.java,
                DB_NAME).fallbackToDestructiveMigration().build()

        fun getDatabase(context: Context): TodoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
    }
}