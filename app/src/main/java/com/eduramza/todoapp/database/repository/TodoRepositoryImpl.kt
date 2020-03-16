package com.eduramza.todoapp.database.repository

import com.eduramza.todoapp.database.model.Todo
import com.eduramza.todoapp.database.source.TodoDao

class TodoRepositoryImpl(private val todoDao: TodoDao): TodoRepository{

    override suspend fun insertTask(text: String): List<Todo>  {
        val todo =
            Todo(text = text)
        todoDao.addItem(todo)
        return getAllTodos()
    }

    override suspend fun getAllTodos() = todoDao.getItems()

    override suspend fun deleteTodo(todo: Todo): List<Todo> {
        todoDao.deleteTodo(todo)
        return getAllTodos()
    }

    override suspend fun markComplete(todo: Todo): List<Todo> {
        todoDao.markCompleted(todo.id)
        return getAllTodos()
    }
}