package com.eduramza.todoapp.database.repository

import com.eduramza.todoapp.database.model.Todo

interface TodoRepository {

    suspend fun insertTask(text: String): List<Todo>

    suspend fun getAllTodos(): List<Todo>

    suspend fun markComplete(todo: Todo): List<Todo>

    suspend fun deleteTodo(todo: Todo): List<Todo>
}