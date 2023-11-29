package com.example.myapplication.todoEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(SubtaskTodo.SUBTASK_TABLE_NAME)
data class SubtaskTodo(
    val id: Long,
    @PrimaryKey(autoGenerate = true)
    val subtaskID: Long = 0,
    val subtaskTitle: String,
    val subtaskScheduledDate: String,
    val subtaskScheduledTime: String,
    val isSubtaskCompleted: Boolean
){
    companion object{
        const val SUBTASK_TABLE_NAME = "subtasks"
    }
}