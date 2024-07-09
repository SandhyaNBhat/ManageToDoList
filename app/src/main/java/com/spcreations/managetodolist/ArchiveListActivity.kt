package com.spcreations.managetodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.spcreations.managetodolist.databinding.ActivityArchiveListBinding

class ArchiveListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchiveListBinding
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityArchiveListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        val todoAdapter = TodoListAdapter(emptyList())
        binding.rvArchivetodolist.layoutManager= LinearLayoutManager(this)
        binding.rvArchivetodolist.adapter = todoAdapter


        // Observe the LiveData from ViewModel and update UI accordingly
        todoViewModel.archivedTodoList.observe(this, Observer { todoList ->
            todoAdapter.setItems(todoList)

        })







    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.tasks_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task ->{
                todoViewModel.deleteAllTasks()
                return true

            }
            else ->{
                super.onOptionsItemSelected(item)
            }

        }

    }
}