package com.example.myapplication.todoEntities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(SubtaskTodo.SUBTASK_TABLE_NAME, foreignKeys = [
    ForeignKey(
        entity = Todo::class,
        parentColumns = ["id"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE
    )
])
data class SubtaskTodo(
    val id: Long,
    @PrimaryKey(autoGenerate = true)
    val subtaskID: Long = 0,
    var subtaskTitle: String,
    var subtaskScheduledDate: String,
    var subtaskScheduledTime: String,
    var subtaskReminderDate: String,
    var subtaskReminderTime: String,
    val isSubtaskCompleted: Boolean
){
    companion object{
        const val SUBTASK_TABLE_NAME = "subtasks"
    }
}