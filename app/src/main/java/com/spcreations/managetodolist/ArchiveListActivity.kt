package com.spcreations.managetodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
}