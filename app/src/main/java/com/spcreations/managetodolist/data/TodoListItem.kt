package com.spcreations.managetodolist.data

import androidx.room.PrimaryKey

data class TodoListItem(
    val taskId: Long,
    val taskName: String,
    val dueDate: Long? = null,
    val category: String,
    val categoryId: Long
)