package com.eduramza.todoapp.database.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.eduramza.todoapp.database.model.Todo

@Dao
interface TodoDao {

    @Insert
    suspend fun addItem(todo: Todo)

    @Query("SELECT * FROM Todo ORDER BY completed, id")
    suspend fun getItems(): List<Todo>

    @Query("UPDATE Todo SET completed = 1 WHERE id = :id")
    suspend fun markCompleted(id: Int)

    @Delete
    suspend fun deleteTodo(todo: Todo)
}