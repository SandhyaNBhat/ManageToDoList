package com.spcreations.managetodolist

import androidx.lifecycle.LiveData
import com.spcreations.managetodolist.data.TaskCategory
import com.spcreations.managetodolist.data.Todo
import com.spcreations.managetodolist.data.TodoDao
import com.spcreations.managetodolist.data.TodoListItem

// TodoRepository.kt
class TodoRepository(private val todoDao: TodoDao) {
    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()

    val todoList : LiveData<List<TodoListItem>> = todoDao.getToDoList()
    val todoListOverdue : LiveData<List<TodoListItem>> = todoDao.getToDoListOverdue()
    val categories: LiveData<List<String>> = todoDao.getCategories()
    val archivedTodoList :LiveData<List<TodoListItem>> = todoDao.getArchivedToDoList()


    suspend fun getCatSpinnerId(categoryName: String ) :Long = todoDao.getCatSpinnerId(categoryName)





     fun getTaskId(taskName:String):LiveData<Long>{
       return todoDao.getTaskId(taskName)
    }



    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }

    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }

    suspend fun insertCategory(category: TaskCategory) {
        todoDao.insertCategory(category)
    }

    suspend fun deleteCategory(category: TaskCategory) {
        todoDao.deleteCategory(category)
    }

    suspend fun updateCategory(category: TaskCategory){
        todoDao.updateCategory(category)
    }

    suspend fun updateCompletedTask(taskName:String){
        todoDao.updateCompletedTask(taskName)
    }

    suspend fun archiveAllTasks() = todoDao.archiveAllTasks()

    suspend fun deleteAllTasks() = todoDao.deleteAllTasks()
}