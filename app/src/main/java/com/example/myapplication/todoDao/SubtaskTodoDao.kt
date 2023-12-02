package com.example.myapplication.todoDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.todoEntities.SubtaskTodo
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtaskTodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubtask(subtaskTodo: SubtaskTodo)

    @Query("SELECT * FROM " + SubtaskTodo.SUBTASK_TABLE_NAME)
    fun getAllSubtasks(): Flow<List<SubtaskTodo>>

    @Query("SELECT * FROM "+ SubtaskTodo.SUBTASK_TABLE_NAME+ " WHERE id= :id")
    fun getSubtasksById(id: Long): Flow<List<SubtaskTodo>>

}