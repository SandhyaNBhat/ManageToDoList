package com.spcreations.managetodolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val task: String,
    val isCompleted: Boolean = false,
    val dueDate: Long? = null,
    val categoryId: Long? = null
)

@Entity(tableName = "task_categories")
data class TaskCategory(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long = 0,
    val categoryName: String
)

