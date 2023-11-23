package com.example.myapplication.todoEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Todo.kt
@Entity(Todo.TABLE_NAME)
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String
){
    companion object{
        const val TABLE_NAME = "todos"
    }
}