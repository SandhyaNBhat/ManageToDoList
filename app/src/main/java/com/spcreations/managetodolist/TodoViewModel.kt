package com.spcreations.managetodolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.spcreations.managetodolist.data.TaskCategory
import com.spcreations.managetodolist.data.Todo
import com.spcreations.managetodolist.data.TodoDatabase
import com.spcreations.managetodolist.data.TodoListItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TodoRepository
    val allTodos: LiveData<List<Todo>>
    val todoList: LiveData<List<TodoListItem>>
    val todoListOverdue: LiveData<List<TodoListItem>>
    val archivedTodoList: LiveData<List<TodoListItem>>
    val categories: LiveData<List<String>>
    val catSpinnerId: Long? =null

    init {
        val todoDao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(todoDao)
        allTodos = repository.allTodos
        todoList = repository.todoList
        todoListOverdue = repository.todoListOverdue
        archivedTodoList = repository.archivedTodoList
        categories = repository.categories
    }

    fun insert(todo: Todo) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun update(todo: Todo) = viewModelScope.launch {
        repository.update(todo)
    }

     fun getTaskId(taskName:String):LiveData<Long> = repository.getTaskId(taskName)

     fun getCatSpinnerId(categoryName: String ) : Job = viewModelScope.launch{
         repository.getCatSpinnerId(categoryName)
     }



    fun delete(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }

    fun insertCategory(taskCategory: TaskCategory) = viewModelScope.launch {
        repository.insertCategory(taskCategory)
    }

    fun updateCompletedTask(taskName: String) = viewModelScope.launch{
        repository.updateCompletedTask(taskName)
    }

    fun archiveAllTasks() = viewModelScope.launch {
        repository.archiveAllTasks()
    }

    fun deleteAllTasks() = viewModelScope.launch {
        repository.deleteAllTasks()
    }
}
