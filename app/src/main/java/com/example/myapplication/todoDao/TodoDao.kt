package com.example.myapplication.todoDao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.todoEntities.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: Todo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(todo:Todo)

//    @Query("Select * from "+Todo.TABLE_NAME+" where todo = :todo")
//    suspend fun getKey(todo:Int):Todo
//
    @Query("Select * from " + Todo.TABLE_NAME+" Order By title ASC")
    fun getAllKeys(): Flow<List<Todo>>

    @Delete
    suspend fun delete(todo:Todo)

    @Query("Delete from todos")
    suspend fun deleteAll()

}