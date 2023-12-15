package com.example.myapplication.todoDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.todoEntities.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: Todo): Long
    @Update
    suspend fun update(todo:Todo)

    @Query("SELECT * FROM "+ Todo.TABLE_NAME + " WHERE isDeleted = false")
    fun getAllTodos(): Flow<List<Todo>>

    @Query("Select * from "+Todo.TABLE_NAME+ " where latitude =:latitude and longitude =:longitude and id=:id")
    fun getLatitudeAndLongitude(latitude:String, longitude:String, id:Long): Flow<List<Todo>>

    @Query("SELECT * FROM "+ Todo.TABLE_NAME)
    fun getAllTableTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM "+Todo.TABLE_NAME+ " WHERE date =:currentDate and isDeleted = false")
    fun getTodosByDate(currentDate: String): Flow<List<Todo>>

    @Query("Update "+Todo.TABLE_NAME+ " set isDeleted = true where id = :id")
    suspend fun delete(id:Long)

    @Query("Delete from "+Todo.TABLE_NAME+ " WHERE id = :id")
    suspend fun deleteFromBin(id:Long)

    @Query("Select * from "+Todo.TABLE_NAME+ " WHERE isDeleted = true")
    fun getDeletedTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM "+Todo.TABLE_NAME+ " WHERE isImportant = true and isDeleted = false")
    fun getImportantTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM "+Todo.TABLE_NAME+ " WHERE scheduledDate IS NOT NULL AND scheduledDate != 'null'  and isDeleted = false")
    fun getScheduledTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM "+Todo.TABLE_NAME+ " WHERE isFavorite = true AND isDeleted = false")
    fun getFavoriteTodos(): Flow<List<Todo>>

    @Query("Update "+Todo.TABLE_NAME+ " set isFavorite = :isFavourite where id = :id")
    suspend fun setFavourite(id:Long, isFavourite: Boolean)

    @Query("Update "+Todo.TABLE_NAME+ " set isImportant = :isImportant where id = :id")
    suspend fun setImportant(id:Long, isImportant: Boolean)

    @Query("SELECT * FROM "+Todo.TABLE_NAME+ " WHERE isFinished = true AND isDeleted = false")
    fun getFinishedTodos(): Flow<List<Todo>>

    @Query("Update "+Todo.TABLE_NAME+ " set isFinished = :isFinished where id = :id")
    suspend fun setFinished(id:Long, isFinished: Boolean)

    @Query("Update "+Todo.TABLE_NAME+ " set isDeleted = false where id = :id")
    suspend fun setNotDeleted(id: Long)

}
