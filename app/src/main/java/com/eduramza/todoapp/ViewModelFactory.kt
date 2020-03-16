package com.eduramza.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eduramza.todoapp.database.repository.TodoRepository

class ViewModelFactory(val todoRepository: TodoRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(TodoRepository::class.java).newInstance(todoRepository)
    }
}