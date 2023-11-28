package com.example.myapplication.todoDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.todoEntities.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: Todo)

    @Query("SELECT * FROM "+ Todo.TABLE_NAME)
    fun getAllTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM "+Todo.TABLE_NAME+ " WHERE date =:currentDate")
    fun getTodosByDate(currentDate: String): Flow<List<Todo>>

    @Query("Delete from "+Todo.TABLE_NAME+ " where id = :id")
    suspend fun delete(id:Long)

    @Query("SELECT * FROM "+Todo.TABLE_NAME+ " WHERE isImportant = true")
    fun getImportantTodos(): Flow<List<Todo>>
}