package com.example.myapplication.todoEntities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.ui.theme.Priority

//import com.example.myapplication.ui.theme.Priority

// Todo.kt
@Entity(Todo.TABLE_NAME)
data class  Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var title: String,
    var description: String,
    val date: String,
    val time: String,
    val isFavorite: Boolean,
    val isImportant: Boolean,
    val scheduledDate: String,
    val scheduledTime: String,
    val reminderDate: String?,
    val reminderTime: String?,
    val isFinished: Boolean,
    val isDeleted: Boolean,
    val priority: Priority,
    val latitude: String?,
    val longitude: String?,
    val imageUri: String? = null
){
    companion object{
        const val TABLE_NAME = "todos"
    }
}