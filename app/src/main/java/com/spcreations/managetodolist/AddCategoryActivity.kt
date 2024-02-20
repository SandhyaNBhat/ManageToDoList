package com.spcreations.managetodolist

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import androidx.lifecycle.ViewModelProvider
import com.spcreations.managetodolist.data.TaskCategory
import com.spcreations.managetodolist.databinding.ActivityAddCategoryBinding


class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AddCategory);
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        val binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        binding.btnAddCategory.setOnClickListener {
            val categoryName = binding.categoryName.editableText
            val taskCategory = TaskCategory(0,categoryName.toString())
            Log.d("TAG","CategoryName" +categoryName.toString())

            viewModel.insertCategory(taskCategory)

           moveBacktoParent()
        }

        //Navigate back to previous screen on cancel button click
        binding.btnCancelCategory.setOnClickListener {
            moveBacktoParent()
        }
    }

    private fun moveBacktoParent() {
        val intent = Intent(this,AddTasksActivity::class.java)
        startActivity(intent)
    }


}