package com.spcreations.managetodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.DynamicColors
import com.spcreations.managetodolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        binding.emptyListTitle.isVisible = false

        val todoAdapter = TodoListAdapter(emptyList())
        val todoOverDueAdapter = TodoListOverdueAdapter(emptyList())

        //setting the adapter for the upcoming todos
        binding.rvTodolist.layoutManager = LinearLayoutManager(this)
        binding.rvTodolist.adapter = todoAdapter

        //setting the adapter for the overdue todos
        binding.rvOverdueList.layoutManager = LinearLayoutManager(this)
        binding.rvOverdueList.adapter = todoOverDueAdapter

        todoAdapter.onCheckBoxClick = {
            todoViewModel.updateCompletedTask(it)
        }

        todoAdapter.onClickListener = {
            val intent = Intent(this, AddTasksActivity::class.java)
            intent.putExtra("task_Id",it.taskId)
            intent.putExtra("task_name",it.taskName)
            intent.putExtra("task_category",it.categoryId)
            Log.d("TAG","Category value sent to intent "+it.categoryId)
            intent.putExtra("task_due_date",it.dueDate)
            Log.d("TAG","Due date value sent to intent "+it.dueDate)
            startActivity(intent)
        }

        todoOverDueAdapter.onCheckBoxClick = {
            todoViewModel.updateCompletedTask(it)
        }

        todoOverDueAdapter.onClickListener = {
            val intent = Intent(this, AddTasksActivity::class.java)
            intent.putExtra("task_Id",it.taskId)
            intent.putExtra("task_name",it.taskName)
            intent.putExtra("task_category",it.categoryId)
            Log.d("TAG","Category value sent to intent "+it.categoryId)
            intent.putExtra("task_due_date",it.dueDate)
            Log.d("TAG","Due date value sent to intent "+it.dueDate)
            startActivity(intent)
        }

        // Observe the LiveData from ViewModel and update UI accordingly
        todoViewModel.todoListOverdue.observe(this, Observer { todoListOverdue ->
            todoOverDueAdapter.setItems(todoListOverdue)
            if (todoOverDueAdapter.itemCount==0){
                binding.linearLayoutOverdue.isVisible = false
                binding.emptyListTitle.isVisible=true
            }else{

                binding.emptyListTitle.isVisible=false
            }
        })

        // Observe the LiveData from ViewModel and update UI accordingly
        todoViewModel.todoList.observe(this, Observer { todoList ->
            todoAdapter.setItems(todoList)
            if(todoAdapter.itemCount==0){
                binding.linearLayoutTodolist.isVisible = false
                binding.emptyListTitle.isVisible=true

            }else{
                binding.emptyListTitle.isVisible=false
            }
        })

       /* if(!upcomingListFlag && !overdueListFlag){
            binding.emptyListTitle.isVisible=true
        }else{
            binding.emptyListTitle.isVisible=false
        }*/







        //Add clicklistener to Fab button

        val addTasksBtn = binding.fab
        addTasksBtn.setOnClickListener {
            val intent = Intent(this, AddTasksActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.archive_todos -> {
                val intent = Intent(this, ArchiveListActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.archive_all -> {
                todoViewModel.archiveAllTasks()
                true
            }
            R.id.delete_todos->{
                todoViewModel.deleteAllTasks()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }


    }
}