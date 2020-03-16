package com.eduramza.todoapp.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val completed: Boolean = false
)