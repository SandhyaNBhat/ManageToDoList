package com.spcreations.managetodolist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_items")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("SELECT Id from todo_items where task =:taskName")
    fun getTaskId(taskName:String): LiveData<Long>

    @Query("SELECT todo_items.task AS taskName,todo_items.dueDate ,task_categories.categoryName as category, todo_items.categoryId as categoryId " +
            "FROM todo_items,task_categories  " +
            "WHERE todo_items.categoryId=task_categories.categoryId " +
            "AND todo_items.isCompleted=false")
    fun getToDoList(): LiveData<List<TodoListItem>>

    @Query("SELECT todo_items.task AS taskName,todo_items.dueDate ,task_categories.categoryName as category, todo_items.categoryId as categoryId " +
            "FROM todo_items,task_categories  " +
            "WHERE todo_items.categoryId=task_categories.categoryId " +
            "AND todo_items.isCompleted=true")
    fun getArchivedToDoList(): LiveData<List<TodoListItem>>

    @Query("SELECT categoryId FROM task_categories WHERE categoryName = :categoryName")
     suspend fun getCatSpinnerId(categoryName: String):Long



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("UPDATE todo_items SET isCompleted=true WHERE task=:taskName")
    suspend fun updateCompletedTask(taskName:String)


    @Query("SELECT * FROM task_categories")
    fun getAllCategories(): LiveData<List<TaskCategory>>

    @Query("SELECT distinct categoryName FROM task_categories")
    fun getCategories(): LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: TaskCategory)

    @Delete
    suspend fun deleteCategory(category: TaskCategory)

    @Update
    suspend fun updateCategory(category: TaskCategory)


}